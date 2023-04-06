package service

import lib.controllerBaseImpl.Controller
import lib.fieldComponent.fieldBaseImpl.{Field, Matrix}
import lib.fieldComponent.{FieldInterface, MatrixInterface}
import lib.fileIOJsonImpl.{FileIO as FileIOJson, FileIO_uPickle as FileIOJson_uPickle}
import lib.fileIOXMLImpl.FileIO as FileIOXml
import lib.{ControllerInterface, FileIOInterface, Player}

class FlexibleModule(rows: Int, cols: Int):
  given MatrixInterface[Player] = new Matrix(cols, rows)

  given ControllerInterface[Player] = Controller()

  given FileIOInterface = FileIOJson()

  given FieldInterface[Player] = Field()

object HexModule extends FlexibleModule(6, 9)
