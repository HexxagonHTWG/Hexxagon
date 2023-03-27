package model

import controller.controllerComponent.controllerBaseImpl.*
import model.GameStatus.*
import model.Player
import model.fieldComponent.fieldBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec:
  "A Player" should {
    "have one value for player 1 aka X" in {
      Player.fromChar('X') should be(Player.X)
    }
    "have one value for player 2 aka O" in {
      Player.fromChar('O') should be(Player.O)
    }
    "have one value for empty space" in {
      Player.fromChar(' ') should be(Player.Empty)
    }
    "have no value for any other character" in {
      Player.fromChar('a') should be(Player.Empty)
    }
    "return the opposite player" in {
      Player.X.other should be(Player.O)
      Player.O.other should be(Player.X)
      Player.Empty.other should be(Player.Empty)
    }
    "be created either" when {
      "parameter is a Char" in {
        Player.fromChar('X') should be(Player.X)
        Player.fromChar('O') should be(Player.O)
        Player.fromChar(' ') should be(Player.Empty)
      }
      "parameter is a String" in {
        Player.fromString("X") should be(Player.X)
        Player.fromString("O") should be(Player.O)
        Player.fromString(" ") should be(Player.Empty)
        Player.fromString("") should be(Player.Empty)
      }
    }
    "have a corresponding game status" when {
      "asking for it's own" in {
        Player.X.gameStatus should be(GameStatus.TURN_PLAYER_1)
        Player.O.gameStatus should be(GameStatus.TURN_PLAYER_2)
        Player.Empty.gameStatus should be(GameStatus.IDLE)
      }
      "asking for the other player" in {
        Player.X.other.gameStatus should be(GameStatus.TURN_PLAYER_2)
        Player.O.other.gameStatus should be(GameStatus.TURN_PLAYER_1)
        Player.Empty.other.gameStatus should be(GameStatus.IDLE)
      }
    }
  }
