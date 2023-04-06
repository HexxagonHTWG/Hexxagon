package lib

import lib.GameStatus.*
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
    "have a type with a value of the given ENUMS" in {
      GameStatus.values should be(Array(IDLE, GAME_OVER, TURN_PLAYER_1, TURN_PLAYER_2))
    }
  }
