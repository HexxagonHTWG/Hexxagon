package _main_

import controller.controllerComponent.{ControllerInterface, controllerBaseImpl}
import model.fieldComponent.fieldBaseImpl.{Field, Matrix}
import model.fieldComponent.{FieldInterface, MatrixInterface}
import model.fileIOComponent.FileIOInterface
import model.fileIOComponent.fileIOJsonImpl.FileIO
// import model.fileIOComponent.fileIOXMLImpl.FileIO

class FlexibleModule(rows: Int, cols: Int):
  given MatrixInterface[Char] = new Matrix(cols, rows)

  given FieldInterface[Char] = Field()

  given ControllerInterface[Char] = controllerBaseImpl.Controller()

  given FileIOInterface = FileIO()

object HexModule extends FlexibleModule(6, 9)
