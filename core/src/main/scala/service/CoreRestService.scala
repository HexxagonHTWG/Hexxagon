package service

import cats.effect.*
import com.comcast.ip4s.*
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import di.CoreServerModule.given_ControllerInterface_Player as controller
import di.{CoreModule, PersistenceModule}
import lib.defaultImpl.Controller
import lib.json.HexJson
import lib.{ControllerInterface, Player}
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

import scala.util.{Failure, Success, Try}

object CoreRestService extends IOApp with StrictLogging:

  private lazy val config = ConfigFactory.load()
  private val restController = HttpRoutes.of[IO] {
    case GET -> Root / "field" =>
      Ok(controller.hexField.toString)
    case GET -> Root / "status" =>
      Ok(controller.gameStatus.toString)
    case POST -> Root / "fillAll" / c =>
      controller.fillAll(Player.fromString(c))
      defaultResponse
    case POST -> Root / "save" =>
      controller.save() match
        case Success(_) => defaultResponse
        case Failure(e) =>
          logger.error(e.getMessage)
          InternalServerError("Could not save game")
    case GET -> Root / "load" =>
      controller.load() match
        case Success(_) => defaultResponse
        case Failure(e) =>
          logger.error(e.getMessage)
          InternalServerError("Could not load game")
    case POST -> Root / "place" / c / x / y =>
      controller.place(Player.fromString(c), x.toInt, y.toInt)
      defaultResponse
    case POST -> Root / "undo" =>
      controller.undo()
      defaultResponse
    case POST -> Root / "redo" =>
      controller.redo()
      defaultResponse
    case POST -> Root / "reset" =>
      controller.reset()
      defaultResponse
    case GET -> Root / "exportField" =>
      defaultResponse
  }.orNotFound
  private val loggingService = Logger.httpApp(false, false)(restController)

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString("0.0.0.0").get)
      .withPort(
        Port.fromString(Try(config.getString("http.core.port")).getOrElse("8080")).get
      )
      .withHttpApp(loggingService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)

  private def defaultResponse =
    Ok(HexJson.encode(controller.hexField))
