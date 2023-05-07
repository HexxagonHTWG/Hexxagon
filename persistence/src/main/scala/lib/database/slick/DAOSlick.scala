package lib.database.slick

import di.{FlexibleProviderModule, PersistenceModule}
import lib.{GameStatus, Player}
import lib.database.DAOInterface
import lib.database.slick.tables.{FieldTable, GameTable}
import lib.field.FieldInterface
import play.api.libs.json.Json
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object DAOSlick extends DAOInterface[Player]:
  private val databaseUrl: String =
    "jdbc:mysql://"
      + sys.env.getOrElse("DATABASE_HOST", "localhost:3306") + "/"
      + sys.env.getOrElse("MYSQL_DATABASE", "hexxagon")
      + "?serverTimezone=UTC&useSSL=false"
  private val databaseUser: String = sys.env.getOrElse("MYSQL_USER", "user")
  private val databasePassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "root")

  val database = Database.forURL(
    url = databaseUrl,
    driver = "com.mysql.cj.jdbc.Driver",
    user = databaseUser,
    password = databasePassword
  )

  private val gameTable = new TableQuery(new GameTable(_))
  private val fieldTable = new TableQuery(new FieldTable(_))

  private val setup: DBIOAction[Unit, NoStream, Effect.Schema] =
    DBIO.seq(gameTable.schema.createIfNotExists, fieldTable.schema.createIfNotExists)
  database.run(setup)

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      val insertAction = gameTable returning gameTable.map(_.id)
        += (0, field.matrix.row, field.matrix.col)

      val gameId = Await.result(database.run(insertAction), 5 seconds)

      insertField(gameId, field)
    }

  override def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      val maxGameId = gameTable.map(_.id).max
      val gameAction = gameId.map(id => gameTable.filter(_.id === id))
        .getOrElse(gameTable.filter(_.id === maxGameId))
      val fieldAction = gameId.map(id => fieldTable.filter(_.gameId === id))
        .getOrElse(fieldTable.filter(_.gameId === maxGameId))

      val gameResult = Await.result(database.run(gameAction.result), 2.second)
      val rows = gameResult.head._2
      val cols = gameResult.head._3
      var hexField = FlexibleProviderModule(rows, cols).given_FieldInterface_Player

      val fieldResult = Await.result(database.run(fieldAction.result), 2.second)
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

      Await.result(database.run(gameAction), 5 seconds)
      Await.result(database.run(fieldAction), 5 seconds)

      insertField(gameId, field)
    }

  override def delete(gameId: Option[Int]): Try[Unit] =
    Try {
      val maxGameId = gameTable.map(_.id).max
      val gameAction = gameId.map(id => gameTable.filter(_.id === id).delete)
        .getOrElse(gameTable.filter(_.id === maxGameId).delete)
      val fieldAction = gameId.map(id => fieldTable.filter(_.gameId === id).delete)
        .getOrElse(fieldTable.filter(_.gameId === maxGameId).delete)

      Await.result(database.run(gameAction), 5 seconds)
      Await.result(database.run(fieldAction), 5 seconds)
      Success(())
    }

  private def insertField(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      for (row <- 0 until field.matrix.row) {
        for (col <- 0 until field.matrix.col) {
          val cell = field.matrix.cell(col, row)
          val insertAction = fieldTable += (0, gameId, row, col, cell.toString)

          Await.result(database.run(insertAction), 5 seconds)
        }
      }
    }
