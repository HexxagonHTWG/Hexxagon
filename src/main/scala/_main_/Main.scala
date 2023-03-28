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
    processInputLine()
  }

  private def processInputLine(): Unit =
    val nextLine = readLine()
    val input = tui.handleInput(nextLine)
    input.foreach(println)
    input match {
      case i if i.contains(TUI.exitMes) || i.contains(TUI.filledWithX) || i.contains(TUI.filledWithO) =>
      case _ => Some(processInputLine())
    }

  def runGUI(): Unit =
    GUI().start()

object MainTUI extends App:
  starter.runTUI()

object MainGUI extends JFXApp3:
  override def start(): Unit =
    starter.runGUI()
