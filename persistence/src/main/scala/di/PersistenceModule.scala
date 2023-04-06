package di

import lib.FileIOInterface
import lib.fileIOJsonImpl.{FileIO as FileIOJson, FileIO_uPickle as FileIOJson_uPickle}
import lib.fileIOXMLImpl.FileIO as FileIOXml

object PersistenceModule:
  given FileIOInterface = FileIOJson(using ProviderModule.given_FieldInterface_Player)

object XMLPersistenceModule:
  given FileIOInterface = FileIOXml(using ProviderModule.given_FieldInterface_Player)

object JsonUPicklePersistenceModule:
  given FileIOInterface = FileIOJson_uPickle(using ProviderModule.given_FieldInterface_Player)

class FlexiblePersistenceModule(rows: Int, cols: Int):
  given FileIOInterface = FileIOJson(using FlexibleProviderModule(rows, cols).given_FieldInterface_Player)
