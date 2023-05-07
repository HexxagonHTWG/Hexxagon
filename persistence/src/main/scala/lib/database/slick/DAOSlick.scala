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
      val rows = field.matrix.row
      val cols = field.matrix.col

      val insertAction = gameTable returning gameTable.map(_.id) += (0, rows, cols)
      val insertResult = database.run(insertAction)
      val gameId = Await.result(insertResult, 5 seconds)

      for (row <- 0 until rows) {
        for (col <- 0 until cols) {
          val cell = field.matrix.cell(col, row)
          val insertAction = fieldTable += (0, gameId, row, col, cell.toString)
          val insertResult = database.run(insertAction)
          Await.result(insertResult, 5 seconds)
        }
      }
    }

  def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      val (gameQuery, fieldQuery) = gameId match
        case Some(id) => (gameTable.filter(_.id === id),
          fieldTable.filter(_.gameId === id))
        case None => (gameTable.filter(_.id === gameTable.map(_.id).max),
          fieldTable.filter(_.gameId === fieldTable.map(_.gameId).max))

      val gameResult = Await.result(database.run(gameQuery.result), 2.second)
      val rows = gameResult.head._2
      val cols = gameResult.head._3
      var hexField = FlexibleProviderModule(rows, cols).given_FieldInterface_Player

      val fieldResult = Await.result(database.run(fieldQuery.result), 2.second)
      for (row <- 0 until rows) {
        for (col <- 0 until cols) {
          val cell = fieldResult.filter(_._3 == row).filter(_._4 == col).head._5
          hexField = hexField.placeAlways(Player.fromString(cell), col, row)
        }
      }
      hexField
    }
