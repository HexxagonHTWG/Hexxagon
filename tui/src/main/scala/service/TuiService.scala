package service

import com.typesafe.scalalogging.{Logger, StrictLogging}
import di.{CoreModule, CoreRestModule}
import lib.TUI

import scala.io.StdIn.readLine

object TuiService extends App:
  Starter(TUI(using CoreModule.given_ControllerInterface_Player)).runTUI()

object TuiRestService extends App:
  Starter(TUI(using CoreRestModule.given_ControllerInterface_Player)).runTUI()

case class Starter(tui: TUI):
  def runTUI(): Unit = {
    logger.info(tui.startMessage)
    tui.handleInput("save")
    processInputLine()
  }

  private def processInputLine(): Unit =
    val nextLine = readLine()
    val input = tui.handleInput(nextLine)
    input.foreach(logger.info(_))
    input match {
      case i if i.contains(TUI.exitMes) || i.contains(TUI.filledWithX) || i.contains(TUI.filledWithO) =>
      case _ => Some(processInputLine())
    }

  private def logger = Logger("Tui")
