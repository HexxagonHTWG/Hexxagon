package lib.database.slick

import com.typesafe.config.ConfigFactory
import di.{FlexibleProviderModule, PersistenceModule}
import lib.database.DAOInterface
import lib.database.slick.tables.{FieldTable, GameTable}
import lib.field.FieldInterface
import lib.{GameStatus, Player}
import play.api.libs.json.Json
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import java.sql.SQLNonTransientException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object DAOSlick extends DAOInterface[Player]:

  private lazy val config = ConfigFactory.load()
  private val databaseUrl: String =
    s"jdbc:${config.getString("db.protocol")}://" +
      s"${config.getString("db.host")}:" +
      s"${config.getString("db.port")}/" +
      s"${config.getString("db.name")}?serverTimezone=CET&useSSL=false"

  println(databaseUrl)
  private val database = Database.forURL(
    url = databaseUrl,
    driver = config.getString("db.driver"),
    user = config.getString("db.user"),
    password = config.getString("db.password")
  )

  private val maxWaitTime: Duration = 5 seconds

  private val gameTable = new TableQuery(new GameTable(_))
  private val fieldTable = new TableQuery(new FieldTable(_))

  private val setup: DBIOAction[Unit, NoStream, Effect.Schema] =
    DBIO.seq(gameTable.schema.createIfNotExists, fieldTable.schema.createIfNotExists)

  try {
    Await.result(database.run(setup), maxWaitTime)
  } catch {
    case _: SQLNonTransientException =>
      println("Waiting for DB connection")
      Thread.sleep(5000)
      Await.result(database.run(setup), maxWaitTime)
  }

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      val insertAction = gameTable returning gameTable.map(_.id)
        += (0, field.matrix.row, field.matrix.col)

      val gameId = Await.result(database.run(insertAction), maxWaitTime)

      insertField(gameId, field)
    }

  private def insertField(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      for (row <- 0 until field.matrix.row) {
        for (col <- 0 until field.matrix.col) {
          val cell = field.matrix.cell(col, row)
          val insertAction = fieldTable += (0, gameId, row, col, cell.toString)

          Await.result(database.run(insertAction), maxWaitTime)
        }
      }
    }

  override def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      val maxGameId = gameTable.map(_.id).max
      val gameAction = gameId.map(id => gameTable.filter(_.id === id))
        .getOrElse(gameTable.filter(_.id === maxGameId))
      val fieldAction = gameId.map(id => fieldTable.filter(_.gameId === id))
        .getOrElse(fieldTable.filter(_.gameId === maxGameId))

      val gameResult = Await.result(database.run(gameAction.result), maxWaitTime)
      val rows = gameResult.head._2
      val cols = gameResult.head._3
      var hexField = FlexibleProviderModule(rows, cols).given_FieldInterface_Player

      val fieldResult = Await.result(database.run(fieldAction.result), maxWaitTime)
      for (row <- 0 until rows) {
        for (col <- 0 until cols) {
          val cell = fieldResult.filter(_._3 == row).filter(_._4 == col).head._5
          hexField = hexField.placeAlways(Player.fromString(cell), col, row)
        }
      }
      hexField
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      val gameAction = gameTable.filter(_.id === gameId).update((gameId, field.matrix.row, field.matrix.col))
      val fieldAction = fieldTable.filter(_.gameId === gameId).delete

      Await.result(database.run(gameAction), maxWaitTime)
      Await.result(database.run(fieldAction), maxWaitTime)

      insertField(gameId, field)
    }

  override def delete(gameId: Option[Int]): Try[Unit] =
    Try {
      val maxGameId = gameTable.map(_.id).max
      val gameAction = gameId.map(id => gameTable.filter(_.id === id).delete)
        .getOrElse(gameTable.filter(_.id === maxGameId).delete)
      val fieldAction = gameId.map(id => fieldTable.filter(_.gameId === id).delete)
        .getOrElse(fieldTable.filter(_.gameId === maxGameId).delete)

      Await.result(database.run(gameAction), maxWaitTime)
      Await.result(database.run(fieldAction), maxWaitTime)
      Success(())
    }
