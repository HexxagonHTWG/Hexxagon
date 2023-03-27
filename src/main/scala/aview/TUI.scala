package aview

import _main_.HexModule.given
import controller.controllerComponent.ControllerInterface
import model.Player
import util.Observer

import scala.util.matching.Regex

object TUI:
  val filledWithX = "Filled with X."
  val filledWithO = "Filled with O."
  val exitMes = "Exiting."

class TUI(using controller: ControllerInterface[Player]) extends Observer:
  controller.add(this)

  private val maxIndRow = controller.hexField.matrix.row - 1
  private val maxIndCol = controller.hexField.matrix.col - 1
  private val message = s"Input your x and y Coordinate as followed:\n[ 0-$maxIndCol ] [ 0-$maxIndRow ] [ X | O ] \n"

  def reg: Regex = ("([0-" + maxIndCol + "]\\s[0-" + maxIndRow + "]\\s[XOxo])").r

  override def update(): Unit = println(controller)

  def startMessage: String = "\nWelcome to Hexxagon!\n" + message

  def handleInput(in: String): Option[String] =
    in match {
      case reg(_*) =>
        val (x: Int, y: Int, c: Char) = in.split("\\s") match {
          case Array(x, y, c) => (x.toInt, y.toInt, c.charAt(0))
        }
        controller.place(Player.fromChar(c), x, y)
        None
      case "fill X" | "fill x" =>
        controller.fillAll(Player.X)
        Some(TUI.filledWithX)
      case "fill O" | "fill o" =>
        controller.fillAll(Player.O)
        Some(TUI.filledWithO)
      case "save" => controller.save(); Some("Saved.")
      case "load" => controller.load(); Some("Loaded.")
      case "reset" => controller.reset(); Some("Reset.")
      case "redo" | "r" | "re" => controller.redo(); Some("Redone.")
      case "undo" | "u" | "un" | "z" => controller.undo(); Some("Undone.")
      case "q" | "e" | "exit" | "quit" | "Exit" | "Quit" => Some(TUI.exitMes)
      case _ => Some("Wrong Input.")
    }
