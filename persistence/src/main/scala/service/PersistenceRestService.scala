package service

import cats.effect.*
import com.comcast.ip4s.*
import com.typesafe.config.ConfigFactory
import di.PersistenceModule.given_FileIOInterface_Player as fileIO
import lib.Player
import lib.json.HexJson
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.server.middleware.Logger

import scala.util.{Failure, Success, Try}

object PersistenceRestService extends IOApp:

  private lazy val config = ConfigFactory.load()
  private val restController = HttpRoutes.of[IO] {
    case GET -> Root / "load" =>
      val field = fileIO.load
      Ok(HexJson.encode(field))
    case req@POST -> Root / "save" =>
      req.as[String].flatMap { f =>
        fileIO.save(HexJson.decode(f))
        Ok("Saved")
      }
  }.orNotFound
  private val loggingService = Logger.httpApp(false, false)(restController)

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(
        Host.fromString(Try(config.getString("http.persistence.host")).getOrElse("0.0.0.0")).get
      )
      .withPort(
        Port.fromString(Try(config.getString("http.persistence.port")).getOrElse("9091")).get
      )
      .withHttpApp(loggingService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
