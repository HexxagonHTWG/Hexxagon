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
import scala.concurrent.duration.Duration
import concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

case class CoreRestClient() extends ControllerInterface[Player] with StrictLogging:
  private lazy val config = ConfigFactory.load()
  private val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds

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
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$coreUrl/status"))
    GameStatus.valueOf(getResponseAsString(response) match
      case "" => "ERROR"
      case x => x)

  override def fillAll(c: Player): Unit =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/fillAll/$c"))
    Try(validateResponse(response))

  override def save(): Try[Unit] =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/save",
      entity = HttpEntity(HexJson.encode(hexField))))
    Try(validateResponse(response))

  override def load(): Try[Unit] =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$coreUrl/load"))
    Try(validateResponse(response))

  override def place(c: Player, x: Int, y: Int): Unit =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/place/$c/$x/$y"))
    validateResponse(response)

  override def undo(): Unit =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/undo"))
    validateResponse(response)

  override def redo(): Unit =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/redo"))
    validateResponse(response)

  override def reset(): Unit =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$coreUrl/reset"))
    validateResponse(response)

  private def validate(res: Try[FieldInterface[Player]]): Unit =
    res match
      case Success(value) =>
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to decode field")

  override def exportField: String =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$coreUrl/exportField"))
    getResponseAsString(response)

  private def validateResponse(res: Future[HttpResponse]): Unit =
    validate(HexJson.decode(getResponseAsString(res)))

  private def getResponseAsString(res: Future[HttpResponse]): String =
    val responseAsString: Future[String] = Unmarshal(Await.result(res, maxWaitSeconds).entity).to[String]
    Await.result(responseAsString, maxWaitSeconds)