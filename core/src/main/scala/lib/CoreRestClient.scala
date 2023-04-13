package lib

import di.CoreModule
import geny.Bytes
import lib.field.FieldInterface
import lib.json.HexJson
import requests.{Requester, Response}

import scala.util.{Failure, Success, Try}

case class CoreRestClient() extends ControllerInterface[Player]:
  private val coreUrl = "http://0.0.0.0:8080"
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

  override def undo(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/undo"))
    notifyObservers()

  override def redo(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/redo"))
    notifyObservers()

  override def reset(): Unit =
    hexField = HexJson.decode(http(requests.post, s"$coreUrl/reset"))
    notifyObservers()

  private def http(method: Requester, url: String): String =
    Try(method(url)) match
      case Success(response) => response.text()
      case Failure(exception) => println(exception); ""

  override def exportField: String = http(requests.get, s"$coreUrl/exportField")
