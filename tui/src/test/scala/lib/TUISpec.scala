package lib

import lib.controllerBaseImpl.Controller
import lib.fieldComponent.fieldBaseImpl.{Field, Matrix}
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class TUISpec extends AnyWordSpec:
  "A TUI" when {
    val hex = new Field(using new Matrix(9, 6))
    val tui = TUI(using Controller(using hex))
    "created" should {
      "have regex in matching something like 0 0 X" in {
        val in = "0 0 X"
        var t = false
        in match {
          case tui.reg(_*) => t = true
          case _ =>
        }
        t should be(true)
        tui.handleInput(in) should be(None)
      }
      "not match something like X 1 1" in {
        val in = "X 1 1"
        var t = false
        in match {
          case tui.reg(_*) => t = true
          case _ =>
        }
        t should be(false)
        tui.handleInput(in) should be(Some("Wrong Input."))
      }
      "fill the matrix with the command 'fill _'" in {
        tui.handleInput("fill x") should be(Some("Filled with X."))
        tui.handleInput("fill X") should be(Some("Filled with X."))
        tui.handleInput("fill o") should be(Some("Filled with O."))
        tui.handleInput("fill O") should be(Some("Filled with O."))
      }
      "exit when q | exit | quit | ..." in {
        tui.handleInput("q") should be(Some("Exiting."))
        tui.handleInput("e") should be(Some("Exiting."))
        tui.handleInput("exit") should be(Some("Exiting."))
        tui.handleInput("quit") should be(Some("Exiting."))
        tui.handleInput("Exit") should be(Some("Exiting."))
        tui.handleInput("Quit") should be(Some("Exiting."))
      }
      "reset when reset" in {
        tui.handleInput("reset") should be(Some("Reset."))
      }
      "call redo when redo | r | ..." in {
        tui.handleInput("redo") should be(Some("Redone."))
        tui.handleInput("r") should be(Some("Redone."))
        tui.handleInput("re") should be(Some("Redone."))
      }
      "call undo when undo | u | ..." in {
        tui.handleInput("undo") should be(Some("Undone."))
        tui.handleInput("u") should be(Some("Undone."))
        tui.handleInput("un") should be(Some("Undone."))
        tui.handleInput("z") should be(Some("Undone."))
      }
    }
    "called statically" should {
      "return a string" in {
        TUI.exitMes should be("Exiting.")
        TUI.filledWithO should be("Filled with O.")
        TUI.filledWithX should be("Filled with X.")
      }
    }
  }