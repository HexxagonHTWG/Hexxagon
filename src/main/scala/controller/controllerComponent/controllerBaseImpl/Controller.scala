package controller.controllerComponent.controllerBaseImpl

import _main_.HexModule
import controller.GameStatus
import controller.GameStatus.*
import controller.controllerComponent.ControllerInterface
import controller.controllerComponent.controllerBaseImpl.{PlaceAllCommand, PlaceCommand}
import model.Player
import model.fieldComponent.FieldInterface
import util.{Observable, UndoManager}

import scala.xml.Elem

class Controller(using var hexField: FieldInterface[Player])
  extends ControllerInterface[Player]:

  private val GAME_MAX = hexField.matrix.MAX
  private val undoManager = new UndoManager[FieldInterface[Player]]
  var gameStatus: GameStatus = IDLE
  private var savedStatus = IDLE
  private var lastStatus = IDLE

  override def fillAll(c: Player): Unit =
    hexField = undoManager.doStep(hexField, new PlaceAllCommand(hexField, c))
    checkStat()
    notifyObservers()

  private def checkStat(): Unit =
    if hexField.matrix.xCount == GAME_MAX
      || hexField.matrix.oCount == GAME_MAX
      || hexField.matrix.oCount + hexField.matrix.xCount == GAME_MAX
    then gameStatus = GAME_OVER
    else if emptyMatrix then gameStatus = IDLE

  private def emptyMatrix = !hexField.matrix.matrix.flatten.contains(Player.O) && !hexField.matrix.matrix.flatten.contains(Player.X)

  override def place(c: Player, x: Int, y: Int): Unit =
    if ((gameStatus.equals(TURN_PLAYER_1) & c.equals(Player.O))
      || (gameStatus.equals(TURN_PLAYER_2) & c.equals(Player.X)))
      print("\nNot your turn!\n")
      notifyObservers()
    else if !hexField.matrix.cell(x, y).equals(Player.Empty) then
      println("\nOccupied.")
      notifyObservers()
    else
      undoManager.redoStack = Nil
      hexField = undoManager.doStep(hexField, new PlaceCommand(hexField, c, x, y))
      lastStatus = gameStatus
      checkStat()
      if gameStatus == GAME_OVER then notifyObservers()
      else c match {
        case Player.X => gameStatus = TURN_PLAYER_2
        case Player.O => gameStatus = TURN_PLAYER_1
        case _ => ()
      }
      notifyObservers()

  override def undo(): Unit =
    if emptyMatrix || gameStatus == GAME_OVER then notifyObservers()
    else
      val mem = gameStatus
      hexField = undoManager.undoStep(hexField)
      gameStatus = lastStatus
      lastStatus = mem
      checkStat()
      notifyObservers()

  override def redo(): Unit =
    if undoManager.redoStack == Nil then notifyObservers()
    else
      if lastStatus == IDLE then lastStatus = TURN_PLAYER_1
      val mem = lastStatus
      lastStatus = gameStatus
      hexField = undoManager.redoStep(hexField)
      gameStatus = mem
      checkStat()
      notifyObservers()

  override def reset(): Unit =
    hexField = hexField.reset
    gameStatus = IDLE
    undoManager.undoStack = Nil
    undoManager.redoStack = Nil
    notifyObservers()

  override def save(): Unit =
    HexModule.given_FileIOInterface.save(hexField)
    savedStatus = gameStatus
    notifyObservers()

  override def load(): Unit =
    hexField = HexModule.given_FileIOInterface.load
    gameStatus = savedStatus
    checkStat()
    notifyObservers()

  override def toString: String =
    gameStatus.message() + hexField.toString
      + "\nX: " + hexField.matrix.xCount + "\tO: " + hexField.matrix.oCount
      + "\n" + "_" * (4 * hexField.matrix.col + 1) + "\n"

  override def exportField: String =
    HexModule.given_FileIOInterface.exportGame(hexField, hexField.matrix.xCount, hexField.matrix.oCount,
      if (gameStatus.message().equals(TURN_PLAYER_2.message())) 2 else 1)
