package service

import lib.GUI
import scalafx.application.JFXApp3
import service.HexModule.given_ControllerInterface_Player

object GuiService extends JFXApp3:
  override def start(): Unit =
    GUI().start()
