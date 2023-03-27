package _main_

import _main_.HexModule.given
import aview.TUI
import aview.gui.GUI
import controller.controllerComponent.ControllerInterface
import controller.controllerComponent.controllerBaseImpl.Controller
import scalafx.application.JFXApp3

import scala.io.StdIn.readLine

object starter:
  val tui: TUI = TUI()

  def runTUI(): Unit = {
    println(tui.startMessage)
    tui.handleInput("save")
    var input = ""
    var tmp = tui.handleInput(input)
    while (!tmp.contains(TUI.exitMes) & !tmp.contains(TUI.filledWithX) & !tmp.contains(TUI.filledWithO)) {
      input = readLine()
      tmp = tui.handleInput(input)
      if tmp.isDefined then println(tmp.get)
    }
  }

  def runGUI(): Unit =
    GUI().start()

object MainTUI extends App:
  starter.runTUI()

object MainGUI extends JFXApp3:
  override def start(): Unit =
    starter.runGUI()
