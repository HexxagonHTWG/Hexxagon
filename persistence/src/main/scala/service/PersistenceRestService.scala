package service

import cats.effect.*
import com.comcast.ip4s.*
import com.typesafe.config.ConfigFactory
import di.PersistenceModule.given_FileIOInterface_Player as fileIO
import di.PersistenceRestModule.given_DAOInterface_Player as dao
import lib.Player
import lib.field.FieldInterface
import lib.json.HexJson
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.server.middleware.Logger

import scala.util.{Failure, Success, Try}

object PersistenceRestService extends IOApp:

  private lazy val config = ConfigFactory.load()
  private val restController = HttpRoutes.of[IO] {
    case req@POST -> Root / "save" =>
      saveBody(req, dao.save(_))
    case GET -> Root / "load" =>
      dao.load(None) match
        case Success(field) => Ok(HexJson.encode(field))
        case Failure(_) => InternalServerError("Could not load game")
    case GET -> Root / "loadFile" =>
      fileIO.load match
        case Success(field) => Ok(HexJson.encode(field))
        case Failure(_) => InternalServerError("Could not load game")
    case req@POST -> Root / "saveFile" =>
      saveBody(req, fileIO.save(_))
  }.orNotFound
  private val loggingService = Logger.httpApp(false, false)(restController)

  private def saveBody(req: Request[IO], save: FieldInterface[Player] => Unit): IO[Response[IO]] =
    req.as[String].flatMap { f =>
      HexJson.decode(f) match
        case Success(field) =>
          save(field)
          Ok("Saved")
        case Failure(_) =>
          BadRequest("Invalid field")
    }

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString("0.0.0.0").get)
      .withPort(
        Port.fromString(Try(config.getString("http.persistence.port")).getOrElse("9091")).get
      )
      .withHttpApp(loggingService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
