package controller.controllerComponent.controllerBaseImpl

import controller.controllerComponent.CommandTemplate
import model.fieldComponent.FieldInterface

class PlaceCommand[T <: FieldInterface[Char]](field: T, content: Char, x: Int, y: Int) extends CommandTemplate(field: T):
  override def command: T = field.place(content, x, y).asInstanceOf[T]

class PlaceAllCommand[T <: FieldInterface[Char]](field: T, content: Char) extends CommandTemplate(field: T):
  override def command: T = field.fillAll(content).asInstanceOf[T]
