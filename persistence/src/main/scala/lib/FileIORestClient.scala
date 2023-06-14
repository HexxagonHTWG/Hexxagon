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
import scala.concurrent.duration.Duration
import concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

case class FileIORestClient() extends FileIOInterface[Player] with StrictLogging:
  private lazy val config = ConfigFactory.load()
  private val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds

  private val persistenceUrl =
    Try(s"http://${config.getString("http.persistence.host")}:${config.getString("http.persistence.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(exception.getMessage); "http://0.0.0.0:8081"
  private val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")
  given ActorSystem[Any] = system
  
  override def load: Try[FieldInterface[Player]] =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$persistenceUrl/load"))
    HexJson.decode(getResponseAsString(response))

  override def save(field: FieldInterface[Player]): Try[Unit] =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"$persistenceUrl/save",
      entity = HexJson.encode(field)))
    processResponse(response)

  override def exportGame(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): String =
    val response: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = s"$persistenceUrl/exportField"))
    getResponseAsString(response)

  private def processResponse(res: Future[HttpResponse]): Try[Unit] =
    Await.result(res, maxWaitSeconds) match
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Success(())
      case HttpResponse(code, _, entity, _) =>
        logger.error(s"Request failed with code $code and entity $entity")
        Failure(new Exception(s"Request failed with code $code and entity $entity"))

  private def decodeResponse(res: Future[HttpResponse]): Try[FieldInterface[Player]] =
    HexJson.decode(getResponseAsString(res))

  private def getResponseAsString(res: Future[HttpResponse]): String =
    val responseAsString: Future[String] = Unmarshal(Await.result(res, maxWaitSeconds).entity).to[String]
    Await.result(responseAsString, maxWaitSeconds)
