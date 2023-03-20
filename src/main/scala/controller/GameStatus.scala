package controller

enum GameStatus(mes: String):
  case IDLE extends GameStatus("")
  case GAME_OVER extends GameStatus("GAME OVER")
  case TURN_PLAYER_1 extends GameStatus("Player 1's turn to place X")
  case TURN_PLAYER_2 extends GameStatus("Player 2's turn to place O")

  def message(): String = mes
end GameStatus
