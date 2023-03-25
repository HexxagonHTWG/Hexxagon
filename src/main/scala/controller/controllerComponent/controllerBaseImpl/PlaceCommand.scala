package controller.controllerComponent.controllerBaseImpl

import controller.controllerComponent.CommandTemplate
import model.Player
import model.fieldComponent.FieldInterface

class PlaceCommand[T <: FieldInterface[Player]](field: T, content: Player, x: Int, y: Int) extends CommandTemplate(field: T):
  override def command: T = field.place(content, x, y).asInstanceOf[T]

class PlaceAllCommand[T <: FieldInterface[Player]](field: T, content: Player) extends CommandTemplate(field: T):
  override def command: T = field.fillAll(content).asInstanceOf[T]
