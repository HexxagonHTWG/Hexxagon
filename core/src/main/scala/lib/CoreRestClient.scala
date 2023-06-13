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
import di.CoreModule
import geny.Bytes
import lib.Http.fetch
import lib.field.FieldInterface
import lib.json.HexJson
import requests.{Requester, Response, get, post}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

case class CoreRestClient() extends ControllerInterface[Player] with StrictLogging:
  private lazy val config = ConfigFactory.load()
  private val fallBackUrl = "http://0.0.0.0:8080"
  private val coreUrl =
    Try(s"http://${config.getString("http.core.host")}:${config.getString("http.core.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl
  private val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")
  given ActorSystem[Any] = system

  var hexField: FieldInterface[Player] =
    HexJson.decode(exportField) match
      case Success(value) => value
      case Failure(_) => null

  override def gameStatus: GameStatus =
    var res: GameStatus = null
    Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$coreUrl/status"))
    .onComplete {
      case Success(value) => value.entity.asInstanceOf[String] match
        case "" => res = GameStatus.valueOf("ERROR")
        case x => res = GameStatus.valueOf(x)
      case Failure(e) => throw e
    }
    res

  override def fillAll(c: Player): Unit =
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/fillAll/$c"))
      .onComplete {
        case Success(value) => validate(HexJson.decode(value.toString))
        case Failure(e) => throw e
      }

  override def save(): Try[Unit] =
    var res: Try[Unit] = null
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/save",
      entity = HttpEntity(HexJson.encode(hexField))))
      .onComplete {
        case Success(value) => res = Try(validate(HexJson.decode(value.toString)))
        case Failure(e) => throw e
      }
    res

  override def load(): Try[Unit] =
    var res: Try[Unit] = null
    Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$coreUrl/load"))
      .onComplete {
        case Success(value) => res = Try(validate(HexJson.decode(value.toString)))
        case Failure(e) => throw e
      }
    res

  override def place(c: Player, x: Int, y: Int): Unit =
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/place/$c/$x/$y"))
      .onComplete {
        case Success(value) => validate(HexJson.decode(value.toString))
        case Failure(e) => throw e
      }

  override def undo(): Unit =
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/undo"))
      .onComplete {
        case Success(value) => validate(HexJson.decode(value.toString))
        case Failure(e) => throw e
      }

  override def redo(): Unit =
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/redo"))
      .onComplete {
        case Success(value) => validate(HexJson.decode(value.toString))
        case Failure(e) => throw e
      }

  override def reset(): Unit =
    Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/reset"))
      .onComplete {
        case Success(value) => validate(HexJson.decode(value.toString))
        case Failure(e) => throw e
      }

  private def validate(res: Try[FieldInterface[Player]]): Unit =
    res match
      case Success(value) =>
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to decode field")

  override def exportField: String =
    var res: String = null
    Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$coreUrl/exportField"))
      .onComplete {
        case Success(value) => res = value.entity.asInstanceOf[String]
        case Failure(e) => throw e
      }
    res
