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
  private val coreUrl =
    Try(s"http://${config.getString("http.core.host")}:${config.getString("http.core.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(exception.getMessage); "http://0.0.0.0:8080"

  var hexField: FieldInterface[Player] = HexJson.decode(exportField)

  override def gameStatus: GameStatus = GameStatus.valueOf(
    fetch(get, s"$coreUrl/status") match
      case "" => "ERROR"
      case x => x
  )

  override def fillAll(c: Player): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/fillAll/$c"))
    notifyObservers()

  override def save(): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/save"))
    notifyObservers()

  override def load(): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/load"))
    notifyObservers()

  override def place(c: Player, x: Int, y: Int): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/place/$c/$x/$y"))
    notifyObservers()

  override def undo(): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/undo"))
    notifyObservers()

  override def redo(): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/redo"))
    notifyObservers()

  override def reset(): Unit =
    hexField = HexJson.decode(fetch(post, s"$coreUrl/reset"))
    notifyObservers()

  override def exportField: String = fetch(get, s"$coreUrl/exportField")
