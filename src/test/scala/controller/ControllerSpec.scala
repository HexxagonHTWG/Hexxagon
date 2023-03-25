package controller

import controller.GameStatus.*
import controller.controllerComponent.controllerBaseImpl.Controller
import model.Player
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
        controller.place(Player.O, 0, 0)
        obs.updated should be(true)
        controller.hexField.matrix.cell(0, 0) should be(Player.O)
        obs.updated = false
        controller.place(Player.X, 0, 0)
        obs.updated should be(true)
      }
      "notify its observer after filling the Field" in {
        controller.fillAll(Player.O)
        obs.updated should be(true)
        val tmp = controller.hexField.matrix.matrix.flatten
        tmp.contains(Player.X) should be(false)
        tmp.contains(Player.Empty) should be(false)
        tmp.contains(Player.O) should be(true)
      }
      "notify its observers after resetting" in {
        controller.reset()
        obs.updated should be(true)
        val tmp = controller.hexField.matrix.matrix.flatten
        tmp.contains(Player.X) should be(false)
        tmp.contains(Player.Empty) should be(true)
        tmp.contains(Player.O) should be(false)
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
        controller.place(Player.O, 2, 2)
        val status = controller.gameStatus
        controller.hexField.matrix.cell(2, 2) should be(Player.O)
        controller.undo()
        controller.gameStatus should be(orig_status)
        controller.hexField.matrix.cell(2, 2) should be(stone)
        controller.redo()
        controller.gameStatus should be(status)
        controller.hexField.matrix.cell(2, 2) should be(Player.O)
      }
      "change its gamestatus dependent on players turn" in {
        controller.gameStatus should be(TURN_PLAYER_1)
        controller.place(Player.X, 4, 2)
        controller.gameStatus should be(TURN_PLAYER_2)
        controller.place(Player.O, 5, 2)
        controller.gameStatus should be(TURN_PLAYER_1)
      }
      "don't place a stone if it's not the players turn" in {
        val stone = controller.hexField.matrix.cell(1, 1)
        controller.place(Player.X, 0, 0)
        controller.place(Player.X, 1, 1)
        obs.updated should be(true)
        controller.hexField.matrix.cell(1, 1) should be(stone)
        val stone2 = controller.hexField.matrix.cell(4, 4)
        controller.place(Player.O, 3, 3)
        controller.place(Player.O, 4, 4)
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
        controller.place(Player.X, 0, 0)
        controller.gameStatus should be(GAME_OVER)
        controller.fillAll(Player.X)
        controller.gameStatus should be(GAME_OVER)
        controller.fillAll(Player.O)
        controller.gameStatus should be(GAME_OVER)
      }
    }
  }

case class Obs() extends Observer:
  var updated = false

  override def update(): Unit = updated = true
