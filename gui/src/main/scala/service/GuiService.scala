package service

import di.{CoreModule, RestModule}
import lib.GUI
import scalafx.application.JFXApp3

object GuiService extends JFXApp3:
  override def start(): Unit =
    GUI(using CoreModule.given_ControllerInterface_Player).start()

object GuiRestService extends JFXApp3:
  override def start(): Unit =
    GUI(using RestModule.given_ControllerInterface_Player).start()
