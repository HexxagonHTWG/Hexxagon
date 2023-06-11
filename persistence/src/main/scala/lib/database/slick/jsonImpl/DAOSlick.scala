package lib.database.slick.jsonImpl

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import lib.database.DAOInterface
import lib.database.slick.SlickBase
import lib.database.slick.jsonImpl.tables.GameJson
import lib.field.FieldInterface
import lib.json.HexJson
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

  init(DBIO.seq(gameTable.schema.createIfNotExists))

  override def save(field: FieldInterface[Player]): Future[Try[Unit]] =
    Future {
      val currentGameId = gameIdCounter % maxGameCount
      Await.result(update(currentGameId, field), maxWaitSeconds) match
        case Success(_) => Success(gameIdCounter += 1)
        case Failure(e) => Failure(e)
    }
  
  override def update(gameId: Int, field: FieldInterface[Player]): Future[Try[Unit]] =
    Future {
      val gameAction = gameTable.insertOrUpdate((gameId, HexJson.encode(field)))

      Await.result(database.run(gameAction), maxWaitSeconds)
      Success(())
    }

  override def delete(gameId: Option[Int]): Future[Try[Unit]] =
    Future {
      val finalGameId: Int = gameId.getOrElse(gameTable.map(_.id).max.asInstanceOf[Int])
      val gameAction = gameTable.filter(_.id === finalGameId).delete

      Await.result(database.run(gameAction), maxWaitSeconds)
      Success(())
    }

  private def gameTable = new TableQuery(new GameJson(_))

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

      val gameResult = Await.result(database.run(gameAction.result), maxWaitSeconds)
      HexJson.decode(gameResult.head._2)
    }
