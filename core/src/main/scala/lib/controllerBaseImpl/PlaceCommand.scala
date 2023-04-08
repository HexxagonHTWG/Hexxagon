package lib.controllerBaseImpl

import lib.fieldComponent.FieldInterface
import lib.{Command, Player}

case class GenericCommand[T <: FieldInterface[Player]](field: T, function: T => T) extends Command[T]:
  private var fieldCache: T = field

  override def noStep(t: T): T = field

  override def doStep(t: T): T =
    fieldCache = t
    command

  override def undoStep(t: T): T =
    val (oldField, f) = (field, fieldCache)
    fieldCache = oldField
    f

  override def redoStep(t: T): T =
    fieldCache = t
    command

  def command: T = function(field)

class PlaceCommand[T <: FieldInterface[Player]](field: T, content: Player, x: Int, y: Int) extends GenericCommand(field: T, (t: T) => t.place(content, x, y).asInstanceOf[T])

class PlaceAllCommand[T <: FieldInterface[Player]](field: T, content: Player) extends GenericCommand(field: T, (t: T) => t.fillAll(content).asInstanceOf[T])
