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

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      val currentGameId = gameIdCounter % maxGameCount
      update(currentGameId, field) match
        case Success(_) => gameIdCounter += 1
        case Failure(e) => throw e
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      val gameAction = gameTable.insertOrUpdate((gameId, HexJson.encode(field)))

      Await.result(database.run(gameAction), maxWaitSeconds)
      Success(())
    }

  override def delete(gameId: Option[Int]): Try[Unit] =
    Try {
      val finalGameId: Int = gameId.getOrElse(gameTable.map(_.id).max.asInstanceOf[Int])
      val gameAction = gameTable.filter(_.id === finalGameId).delete

      Await.result(database.run(gameAction), maxWaitSeconds)
      Success(())
    }

  private def gameTable = new TableQuery(new GameJson(_))

  override def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      val searchId = gameId.getOrElse((gameIdCounter match {
        case 0 => 0;
        case _ => gameIdCounter - 1
      }) % maxGameCount)
      val gameAction = gameTable
        .filter(
          _.id === searchId
        )

      val gameResult = Await.result(database.run(gameAction.result), maxWaitSeconds)
      HexJson.decode(gameResult.head._2) match
        case Success(field) => field
        case Failure(e) => throw e
    }
