package lib.database.slick.defaultImpl

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import di.{FlexibleProviderModule, PersistenceModule}
import lib.database.DAOInterface
import lib.database.slick.SlickBase
import lib.database.slick.defaultImpl.tables.{Field, Game}
import lib.field.FieldInterface
import lib.{GameStatus, Player}
import play.api.libs.json.Json
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import java.sql.SQLNonTransientException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt, SECONDS}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.control.Breaks.{break, breakable}
import scala.util.{Failure, Success, Try}

object DAOSlick extends DAOInterface[Player] with SlickBase:

  init(DBIO.seq(gameTable.schema.createIfNotExists, fieldTable.schema.createIfNotExists))

  override def save(field: FieldInterface[Player]): Future[Any] =
    val currentGameId = gameIdCounter % maxGameCount
    update(currentGameId, field).andThen { x =>
      gameIdCounter += 1
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Future[Any] =
      val gameAction = gameTable.insertOrUpdate((gameId, field.matrix.row, field.matrix.col))
      val fieldAction = fieldTable.filter(_.gameId === gameId).delete

      database.run(gameAction).andThen(_ => database.run(fieldAction)).andThen(_ => insertField(gameId, field))

  private def insertField(gameId: Int, field: FieldInterface[Player]): Future[Any] =
    Future {
      for (row <- 0 until field.matrix.row) {
        for (col <- 0 until field.matrix.col) {
          val cell = field.matrix.cell(col, row)
          val insertAction = fieldTable += (gameId, row, col, cell.toString)

          database.run(insertAction)
        }
      }
    }

  override def delete(gameId: Option[Int]): Future[Any] =
    val finalGameId: Int = gameId.getOrElse(gameTable.map(_.id).max.asInstanceOf[Int])
    val gameAction = gameTable.filter(_.id === finalGameId).delete
    val fieldAction = fieldTable.filter(_.gameId === finalGameId).delete

    database.run(gameAction).andThen(_ => database.run(fieldAction))

  override def load(gameId: Option[Int]): Future[Try[FieldInterface[Player]]] =
    Future {
      val searchId = gameId.getOrElse((gameIdCounter match {
        case 0 => 0;
        case _ => gameIdCounter - 1
      }) % maxGameCount)
      val gameAction = gameTable
        .filter(
          _.id === searchId
        )
      val fieldAction = fieldTable
        .filter(
          _.gameId === searchId
        )

      val gameResult = Await.result(database.run(gameAction.result), maxWaitSeconds)
      val fieldResult = Await.result(database.run(fieldAction.result), maxWaitSeconds)
      
      (gameResult.size, fieldResult.size) match {
        case (0, _) => Failure(new SQLNonTransientException("No game found"))
        case (_, 0) => Failure(new SQLNonTransientException("No field found"))
        case (_, _) => val rows = gameResult.head._2
          val cols = gameResult.head._3
          var hexField = FlexibleProviderModule(rows, cols).given_FieldInterface_Player

          for (row <- 0 until rows) {
            for (col <- 0 until cols) {
              val cell = fieldResult.filter(_._2 == row).filter(_._3 == col).head._4
              hexField = hexField.placeAlways(Player.fromString(cell), col, row)
            }
          }
          Success(hexField)
      }
    }

  private def gameTable = new TableQuery(new Game(_))

  private def fieldTable = new TableQuery(new Field(_))
