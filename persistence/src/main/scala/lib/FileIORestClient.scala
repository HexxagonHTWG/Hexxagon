package lib

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.HttpMethods.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import lib.Http.fetch
import lib.field.FieldInterface
import lib.json.HexJson
import requests.{get, post}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

case class FileIORestClient() extends FileIOInterface[Player] with StrictLogging {
  private lazy val config = ConfigFactory.load()
  private val persistenceUrl =
    Try(s"http://${config.getString("http.persistence.host")}:${config.getString("http.persistence.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(exception.getMessage); "http://0.0.0.0:8081"
  private val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")
  given ActorSystem[Any] = system
  
  override def load: Try[FieldInterface[Player]] =
    var res: Try[FieldInterface[Player]] = Failure(new Exception("Failed to load game"))
    Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$persistenceUrl/load"))
      .onComplete {
        case Failure(_) => Failure(new Exception("Failed to load game"))
        case Success(value) => res = HexJson.decode(value.entity.toString)
      }
    res

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$persistenceUrl/save",
      entity = HexJson.encode(field)))
      .onComplete {
        case Failure(_) => Failure(new Exception("Failed to save game"))
        case Success(_) =>
      }
    Success(())

  override def exportGame(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): String =
    var res: String = ""
    Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$persistenceUrl/load"))
      .onComplete {
        case Failure(_) => Failure(new Exception("Failed to export game"))
        case Success(value) => res = value.entity.toString
      }
    res
}
