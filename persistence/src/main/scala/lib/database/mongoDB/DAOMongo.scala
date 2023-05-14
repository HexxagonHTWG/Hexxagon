package lib.database.mongoDB

import com.mongodb.client.model.UpdateOptions
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import lib.Player
import lib.database.DAOInterface
import lib.field.FieldInterface
import lib.json.HexJson
import org.mongodb.scala.*
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import play.api.libs.json.JsObject

import scala.concurrent.duration.{Duration, DurationInt, SECONDS}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object DAOMongo extends DAOInterface[Player] with StrictLogging {

  private lazy val config = ConfigFactory.load()
  private val databaseUrl: String =
    s"mongodb://" +
      //s"${config.getString("db.mongodb.user")}:" +
      //s"${config.getString("db.mongodb.password")}@" +
      s"${config.getString("db.mongodb.host")}:" +
      s"${config.getString("db.mongodb.port")}"
      //"/?authSource=admin"

  logger.debug(s"Database URL: $databaseUrl")
  private val client: MongoClient = MongoClient(databaseUrl)
  private val db: MongoDatabase = client.getDatabase("hexxagon")
  private val gameCollection: MongoCollection[Document] = db.getCollection("game")

  private val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds
  private val maxGameCount = config.getInt("db.maxGameCount")
  private var gameIdCounter = 0

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      val currentGameId = gameIdCounter % maxGameCount
      update(currentGameId, field)
      gameIdCounter += 1
    }

  override def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      val searchId = gameId.getOrElse((gameIdCounter match {
        case 0 => 0
        case _ => gameIdCounter - 1
      }) % maxGameCount)
      val document = Await.result(gameCollection.find(equal("_id", searchId)).projection(excludeId())
        .first().head(), maxWaitSeconds)
      HexJson.decode(document.toString()).get
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      val json = HexJson.encode(field)

      gameCollection.updateOne(equal("_id", gameId), set("game", json), UpdateOptions().upsert(true))
        .subscribe(new Observer[UpdateResult] {
          override def onNext(result: UpdateResult): Unit = logger.debug(s"Updated: $result with id $gameId")

          override def onError(e: Throwable): Unit = logger.error(s"Failed: $e")

          override def onComplete(): Unit = logger.debug("Completed")
        })
    }

  override def delete(gameId: Option[Int]): Try[Unit] =
    Try {
      val deleteId = gameId.getOrElse({
        Await.result(gameCollection.find().sort(Document("_id" -> -1)).first().head(),
          maxWaitSeconds).get("_id").get.asInt32().getValue
      })

      gameCollection.deleteOne(equal("_id", deleteId))
        .subscribe(new Observer[DeleteResult] {
        override def onNext(result: DeleteResult): Unit = logger.debug(s"Deleted: $result with id $deleteId")

        override def onError(e: Throwable): Unit = logger.error(s"Failed: $e")

        override def onComplete(): Unit = logger.debug("Completed")
      })
    }
}
