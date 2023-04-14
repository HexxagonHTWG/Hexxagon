package service

import cats.effect.*
import com.comcast.ip4s.*
import com.typesafe.config.ConfigFactory
import di.CoreModule.given_ControllerInterface_Player as controller
import di.{CoreModule, PersistenceModule}
import lib.defaultImpl.Controller
import lib.json.HexJson
import lib.{ControllerInterface, Player}
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

import scala.util.Try

object CoreRestService extends IOApp:

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
      controller.save()
      defaultResponse
    case POST -> Root / "load" =>
      controller.load()
      defaultResponse
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
      .withHost(
        Host.fromString(Try(config.getString("http.core.host")).getOrElse("0.0.0.0")).get
      )
      .withPort(
        Port.fromString(Try(config.getString("http.core.port")).getOrElse("8080")).get
      )
      .withHttpApp(loggingService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)

  private def defaultResponse =
    Ok(HexJson.encode(controller.hexField))
