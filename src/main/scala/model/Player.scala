package model

enum Player(value: Char):
  case X extends Player('X')
  case O extends Player('O')
  case Empty extends Player(' ')

  override def toString: String = value.toString

  def other: Player = this match
    case X => O
    case O => X
    case _ => Empty

  def gameStatus: GameStatus = this match
    case X => GameStatus.TURN_PLAYER_1
    case O => GameStatus.TURN_PLAYER_2
    case _ => GameStatus.IDLE

object Player:
  def fromString(s: String): Player = fromChar(s.headOption.getOrElse(' '))

  def fromChar(c: Char): Player = c match
    case 'X' => X
    case 'O' => O
    case _ => Empty
