package di

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import lib.database.DAOInterface
import lib.database.mongoDB.DAOMongo
import lib.database.slick.DAOSlick
import lib.json.{FileIO as FileIOJson, FileIO_uPickle as FileIOJson_uPickle}
import lib.xml.FileIO as FileIOXml
import lib.{FileIOInterface, FileIORestClient, Player}

import scala.util.Try

private lazy val config = ConfigFactory.load()

object PersistenceModule:
  given FileIOInterface[Player] = FileIOJson()

object PersistenceRestModule extends StrictLogging:
  given FileIOInterface[Player] = FileIORestClient()

  given DAOInterface[Player] = Try(config.getString("db.implementation")).getOrElse("").toUpperCase() match
    case "MONGO" | "MONGODB" => DAOMongo
    case "MYSQL" => DAOSlick
    case _ => logger.error("No such database implementation - using default"); DAOSlick

object XMLPersistenceModule:
  given FileIOInterface[Player] = FileIOXml(using ProviderModule.given_FieldInterface_Player)

object JsonUPicklePersistenceModule:
  given FileIOInterface[Player] = FileIOJson_uPickle(using ProviderModule.given_FieldInterface_Player)
