package controller.controllerComponent

import model.fieldComponent.{FieldInterface, MatrixInterface}
import util.Command

trait CommandTemplate[T <: FieldInterface[Char]](field: T) extends Command[T] {
    var fieldCache: T = field

    override def noStep(field: T): T = field

    override def doStep(field: T): T =
        fieldCache = field
        val f = command
        f

    override def undoStep(field: T): T =
        val oldField = field
        val f = fieldCache
        fieldCache = oldField
        f

    override def redoStep(field: T): T =
        fieldCache = field
        val f = command
        f

    def command: T
}
