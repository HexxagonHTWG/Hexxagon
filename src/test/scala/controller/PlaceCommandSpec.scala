package controller

import controller.controllerComponent.controllerBaseImpl.{GenericCommand, PlaceAllCommand, PlaceCommand}
import model.Player
import model.fieldComponent.FieldInterface
import model.fieldComponent.fieldBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.reflect.io.File

class GenericCommandSpec extends AnyWordSpec:
  "A GenericCommand" should {
    var hex = new Field(using new Matrix(9, 6))

    "execute a given function on a field" when {
      "given a function that places a stone" in {
        val genericCommand = GenericCommand(hex, field => field.place(Player.X, 0, 0).asInstanceOf[Field])
        hex = genericCommand.doStep(hex)
        hex.matrix.cell(0, 0) should be(Player.X)
      }
    }
    "act equal to a place command if correct function is given" in {
      val genericCommand = GenericCommand(hex, (field: Field) => field.place(Player.X, 0, 0).asInstanceOf[Field])
      val placeCommand = new PlaceCommand(hex, Player.X, 0, 0)
      placeCommand.doStep(hex) should be(genericCommand.doStep(hex))
    }
  }

class PlaceCommandSpec extends AnyWordSpec:
  "A PlaceCommand" should {
    var hex = new Field(using new Matrix(9, 6))
    val command = new PlaceCommand(hex, Player.X, 0, 0)

    "place a stone" in {
      hex = command.doStep(hex)
      hex.matrix.cell(0, 0) should be(Player.X)
    }

    "capture state of field before changing it" in {
      command.field should be(new Field(using new Matrix(9, 6)))
    }

    "undo and redo a move" in {
      command.undoStep(hex).matrix.cell(0, 0) should be(Player.Empty)
      command.redoStep(hex).matrix.cell(0, 0) should be(Player.X)
    }

    "change nothing" in {
      command.noStep(hex) should be(hex)
    }
  }

class PlaceAllCommandSpec extends AnyWordSpec:

  "A PlaceAllCommand" should {
    var hex = new Field(using new Matrix(9, 6))
    val command = new PlaceAllCommand(hex, Player.X)

    "fill every cell" in {
      hex = command.doStep(hex)
      var i = 0
      for (rows <- hex.matrix.matrix) {
        for (col <- rows)
          if col.equals(Player.X) then i += 1
      }
      i should be(hex.matrix.col * hex.matrix.row)
    }

    "undo and redo a move" in {
      command.undoStep(hex).matrix.matrix should be(new Field(using new Matrix(9, 6)).matrix.matrix)
      command.redoStep(hex).matrix.matrix should be(new Field(using new Matrix(9, 6)).matrix.fillAll(Player.X).matrix)
    }

    "change nothing" in {
      command.noStep(hex) should be(hex)
    }
  }
