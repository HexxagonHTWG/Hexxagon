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
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import play.api.libs.json.JsObject

import scala.concurrent.duration.{Duration, DurationInt, SECONDS}
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object DAOMongo extends DAOInterface[Player] with StrictLogging {

  private lazy val config = ConfigFactory.load()
  private val databaseUrl: String =
    s"${config.getString("db.mongodb.protocol")}://" +
      s"${config.getString("db.user")}:" +
      s"${config.getString("db.password")}@" +
      s"${config.getString("db.host")}:" +
      s"${config.getString("db.mongodb.port")}" +
      "/?authSource=admin"

  logger.debug(s"Database URL: $databaseUrl")
  private val client: MongoClient = MongoClient(databaseUrl)
  private val db: MongoDatabase = client.getDatabase(config.getString("db.name"))
  private val gameCollection: MongoCollection[Document] = db.getCollection("game")

  private val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds
  private val maxGameCount = config.getInt("db.maxGameCount")
  private var gameIdCounter = 0

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      val currentGameId = gameIdCounter % maxGameCount
      update(currentGameId, field) match
        case Success(_) => gameIdCounter += 1
        case Failure(e) => throw e
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      val json = HexJson.encode(field)

      Await.result(
        gameCollection.updateOne(equal("_id", gameId), set("game", json), UpdateOptions().upsert(true))
          .asInstanceOf[SingleObservable[Unit]]
          .head(),
        maxWaitSeconds
      )
    }

  override def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      val searchId = gameId.getOrElse((gameIdCounter match {
        case 0 => 0
        case _ => gameIdCounter - 1
      }) % maxGameCount)
      val document = Await.result(gameCollection.find(equal("_id", searchId)).projection(excludeId())
        .first().head(), maxWaitSeconds)
      HexJson.decode(document.get("game").get.asString().getValue).get
    }

  override def delete(gameId: Option[Int]): Try[Unit] =
    Try {
      val deleteId = gameId.getOrElse({
        Await.result(gameCollection.find().sort(Document("_id" -> -1)).first().head(),
          maxWaitSeconds).get("_id").get.asInt32().getValue
      })

      Await.result(
        gameCollection.deleteOne(equal("_id", deleteId))
          .asInstanceOf[SingleObservable[Unit]]
          .head(),
        maxWaitSeconds
      )
    }
}
