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
    HexJson.decode(fetch(post, s"$coreUrl/fillAll/$c")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to fill all")

  override def save(): Unit =
    HexJson.decode(fetch(post, s"$coreUrl/save")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to save")

  override def load(): Unit =
    HexJson.decode(fetch(get, s"$coreUrl/load")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to load field")

  override def place(c: Player, x: Int, y: Int): Unit =
    HexJson.decode(fetch(post, s"$coreUrl/place/$c/$x/$y")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to place")

  override def undo(): Unit =
    HexJson.decode(fetch(post, s"$coreUrl/undo")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to undo")

  override def redo(): Unit =
    HexJson.decode(fetch(post, s"$coreUrl/redo")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to redo")

  override def reset(): Unit =
    HexJson.decode(fetch(post, s"$coreUrl/reset")) match
      case Success(value) => 
        hexField = value
        notifyObservers()
      case Failure(_) => logger.error("Failed to reset")

  override def exportField: String = fetch(get, s"$coreUrl/exportField")
