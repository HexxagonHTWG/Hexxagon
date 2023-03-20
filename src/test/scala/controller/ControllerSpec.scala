package controller

import controller.GameStatus.*
import controller.controllerComponent.controllerBaseImpl.Controller
import model.fieldComponent.fieldBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import util.Observer

class ControllerSpec extends AnyWordSpec:
  "A Controller" when {
    "observed by an Observer" should {
      val field = new Field(using new Matrix(9, 6))
      val controller = new Controller(using field)
      val obs = Obs()
      controller.add(obs)
      "notify its Observer after placing a stone" in {
        controller.place('O', 0, 0)
        obs.updated should be(true)
        controller.hexField.matrix.cell(0, 0) should be('O')
        obs.updated = false
        controller.place('X', 0, 0)
        obs.updated should be(true)
      }
      "notify its observer after filling the Field" in {
        controller.fillAll('O')
        obs.updated should be(true)
        val tmp = controller.hexField.matrix.matrix.flatten
        tmp.contains('X') should be(false)
        tmp.contains(' ') should be(false)
        tmp.contains('O') should be(true)
      }
      "notify its observers after resetting" in {
        controller.reset()
        obs.updated should be(true)
        val tmp = controller.hexField.matrix.matrix.flatten
        tmp.contains('X') should be(false)
        tmp.contains(' ') should be(true)
        tmp.contains('O') should be(false)
      }
    }
    "having a step made" should {
      val field = new Field(using new Matrix(9, 6))
      val controller = new Controller(using field)
      val obs = Obs()
      controller.add(obs)
      "undo and redo a move" in {
        val stone = controller.hexField.matrix.cell(2, 2)
        val orig_status = controller.gameStatus
        controller.place('O', 2, 2)
        val status = controller.gameStatus
        controller.hexField.matrix.cell(2, 2) should be('O')
        controller.undo()
        controller.gameStatus should be(orig_status)
        controller.hexField.matrix.cell(2, 2) should be(stone)
        controller.redo()
        controller.gameStatus should be(status)
        controller.hexField.matrix.cell(2, 2) should be('O')
      }
      "change its gamestatus dependent on players turn" in {
        controller.gameStatus should be(TURNPLAYER1)
        controller.place('X', 4, 2)
        controller.gameStatus should be(TURNPLAYER2)
        controller.place('O', 5, 2)
        controller.gameStatus should be(TURNPLAYER1)
      }
      "don't place a stone if it's not the players turn" in {
        val stone = controller.hexField.matrix.cell(1, 1)
        controller.place('X', 0, 0)
        controller.place('X', 1, 1)
        obs.updated should be(true)
        controller.hexField.matrix.cell(1, 1) should be(stone)
        val stone2 = controller.hexField.matrix.cell(4, 4)
        controller.place('O', 3, 3)
        controller.place('O', 4, 4)
        obs.updated should be(true)
        controller.hexField.matrix.cell(4, 4) should be(stone2)
      }
    }
    "filled" should {
      val field = new Field(using new Matrix(1, 1))
      val controller = new Controller(using field)
      val obs = Obs()
      controller.add(obs)
      "define a game over" in {
        controller.place('X', 0, 0)
        controller.gameStatus should be(GAME_OVER)
        controller.fillAll('X')
        controller.gameStatus should be(GAME_OVER)
        controller.fillAll('O')
        controller.gameStatus should be(GAME_OVER)
      }
    }
  }

case class Obs() extends Observer:
  var updated = false

  override def update(): Unit = updated = true
