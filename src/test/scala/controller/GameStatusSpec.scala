package controller

import controller.GameStatus.*
import controller.controllerComponent.controllerBaseImpl.*
import model.fieldComponent.fieldBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class GameStatusSpec extends AnyWordSpec:
  "A GameStatus" should {
    "have map descriptions for its status" in {
      IDLE.message() should be("")
      GAME_OVER.message() should be("GAME OVER")
    }
    "have map descriptions for each players turn" in {
      TURN_PLAYER_1.message() should be("Player 1's turn to place X")
      TURN_PLAYER_2.message() should be("Player 2's turn to place O")
    }
    "have values" in {
      GameStatus.valueOf("TURN_PLAYER_2") should be(TURN_PLAYER_2)
      GameStatus.valueOf("TURN_PLAYER_1") should be(TURN_PLAYER_1)
      GameStatus.valueOf("IDLE") should be(IDLE)
      GameStatus.valueOf("GAME_OVER") should be(GAME_OVER)
    }
    "be controlled via Controller" in {
      val c = Controller(using new Field(using new Matrix(9, 6)))
      c.place('X', 0, 0)
      c.gameStatus should be(TURN_PLAYER_2)
      c.gameStatus.message() should be(TURN_PLAYER_2.message())
      c.place('O', 1, 0)
      c.gameStatus should be(TURN_PLAYER_1)
      c.gameStatus.message() should be(TURN_PLAYER_1.message())
    }
    "have a type with a value of the given ENUMS" in {
      GameStatus.values should be(Array(IDLE, GAME_OVER, TURN_PLAYER_1, TURN_PLAYER_2))
    }
  }
