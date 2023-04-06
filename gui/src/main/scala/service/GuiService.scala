package service

import di.CoreModule.given_ControllerInterface_Player
import lib.GUI
import scalafx.application.JFXApp3

object GuiService extends JFXApp3:
  override def start(): Unit =
    GUI().start()
