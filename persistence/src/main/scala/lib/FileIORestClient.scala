package lib

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import lib.Http.fetch
import lib.field.FieldInterface
import lib.json.HexJson
import requests.{get, post}

import scala.util.{Failure, Success, Try}

case class FileIORestClient() extends FileIOInterface[Player] with StrictLogging {

  private lazy val config = ConfigFactory.load()
  private val persistenceUrl =
    Try(s"http://${config.getString("http.persistence.host")}:${config.getString("http.persistence.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(exception.getMessage); "http://0.0.0.0:8081"

  override def load: FieldInterface[Player] = HexJson.decode(fetch(get, s"$persistenceUrl/load"))

  override def save(field: FieldInterface[Player]): Unit = fetch(post, s"$persistenceUrl/save", HexJson.encode(field))

  override def exportGame(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): String = fetch(get, s"$persistenceUrl/load")
}
