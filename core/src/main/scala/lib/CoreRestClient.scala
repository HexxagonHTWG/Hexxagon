package lib

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import di.CoreModule
import geny.Bytes
import lib.field.FieldInterface
import lib.json.HexJson
import requests.{Requester, Response}

import scala.util.{Failure, Success, Try}

case class CoreRestClient() extends ControllerInterface[Player] with StrictLogging:
  private lazy val config = ConfigFactory.load()
  private val coreUrl =
    Try(s"http://${config.getString("http.core.host")}:${config.getString("http.core.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(exception.getMessage); "http://0.0.0.0:8080"

  var hexField: FieldInterface[Player] = HexJson.decode(exportField)

  override def gameStatus: GameStatus = GameStatus.valueOf(
    http(requests.get, s"$coreUrl/status") match
      case "" => "ERROR"
      case x => x
  )

  override def fillAll(c: Player): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/fillAll/$c"))
    notifyObservers()

  override def save(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/save"))
    notifyObservers()

  override def load(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/load"))
    notifyObservers()

  override def place(c: Player, x: Int, y: Int): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/place/$c/$x/$y"))
    notifyObservers()

  private def http(method: Requester, url: String): String =
    Try(method(url)) match
      case Success(response) =>
        val r = response.text()
        logger.debug(
          if r.length > 26 then
            r.substring(0, 26) + "..."
          else
            r
        )
        r
      case Failure(exception) =>
        logger.error(exception.getMessage)
        ""

  override def undo(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/undo"))
    notifyObservers()

  override def redo(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/redo"))
    notifyObservers()

  override def reset(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/reset"))
    notifyObservers()

  override def exportField: String = http(requests.get, s"$coreUrl/exportField")
