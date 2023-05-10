package di

import lib.database.DAOInterface
import lib.database.slick.DAOSlick
import lib.json.{FileIO as FileIOJson, FileIO_uPickle as FileIOJson_uPickle}
import lib.xml.FileIO as FileIOXml
import lib.{FileIOInterface, FileIORestClient, Player}

object PersistenceModule:
  given FileIOInterface[Player] = FileIOJson()

object PersistenceRestModule:
  given FileIOInterface[Player] = FileIORestClient()
  given DAOInterface[Player] = DAOSlick

object XMLPersistenceModule:
  given FileIOInterface[Player] = FileIOXml(using ProviderModule.given_FieldInterface_Player)

object JsonUPicklePersistenceModule:
  given FileIOInterface[Player] = FileIOJson_uPickle(using ProviderModule.given_FieldInterface_Player)
