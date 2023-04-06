package service

import lib.TUI
import service.HexModule.given_ControllerInterface_Player

import scala.io.StdIn.readLine

object TuiService extends App:
  starter.runTUI()

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

