package lib

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import di.CoreModule
import geny.Bytes
import lib.Http.fetch
import lib.field.FieldInterface
import lib.json.HexJson
import requests.{Requester, Response, get, post}

import scala.util.{Failure, Success, Try}

case class CoreRestClient() extends ControllerInterface[Player] with StrictLogging:
  private lazy val config = ConfigFactory.load()
  private val fallBackUrl = "http://0.0.0.0:8080"
  private val coreUrl =
    Try(s"http://${config.getString("http.core.host")}:${config.getString("http.core.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl

  var hexField: FieldInterface[Player] =
    HexJson.decode(exportField) match
      case Success(value) => value
      case Failure(_) => null

  override def gameStatus: GameStatus = GameStatus.valueOf(
    fetch(get, s"$coreUrl/status") match
      case "" => "ERROR"
      case x => x
  )

  override def fillAll(c: Player): Unit =
    validate(HexJson.decode(fetch(post, s"$coreUrl/fillAll/$c")))

  override def save(): Try[Unit] =
    Try {
      validate(HexJson.decode(fetch(post, s"$coreUrl/save")))
      Success(())
    }

  override def load(): Try[Unit] =
    Try {
      validate(HexJson.decode(fetch(get, s"$coreUrl/load")))
      Success(())
    }

  override def place(c: Player, x: Int, y: Int): Unit =
    validate(HexJson.decode(fetch(post, s"$coreUrl/place/$c/$x/$y")))

  override def undo(): Unit =
    validate(HexJson.decode(fetch(post, s"$coreUrl/undo")))

  override def redo(): Unit =
    validate(HexJson.decode(fetch(post, s"$coreUrl/redo")))

  override def reset(): Unit =
    validate(HexJson.decode(fetch(post, s"$coreUrl/reset")))

  private def validate(res: Try[FieldInterface[Player]]): Unit =
    res match
      case Success(value) =>
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to decode field")

  override def exportField: String = fetch(get, s"$coreUrl/exportField")
