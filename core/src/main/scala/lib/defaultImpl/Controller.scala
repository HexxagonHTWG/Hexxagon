package lib.defaultImpl

import com.typesafe.scalalogging.StrictLogging
import lib.*
import lib.GameStatus.*
import lib.field.FieldInterface

import scala.util.{Failure, Success}
import scala.xml.Elem

class Controller(using var hexField: FieldInterface[Player])(using val fileIO: FileIOInterface[Player])
  extends ControllerInterface[Player] with StrictLogging:

  private val GAME_MAX = hexField.matrix.MAX
  private val undoManager = new UndoManager[FieldInterface[Player]]
  var gameStatus: GameStatus = IDLE
  private var savedStatus = gameStatus
  private var lastStatus = gameStatus

  override def fillAll(c: Player): Unit =
    hexField = undoManager.doStep(hexField, new PlaceAllCommand(hexField, c))
    checkStat()
    notifyObservers()

  override def place(c: Player, x: Int, y: Int): Unit =
    if gameStatus.equals(c.other.gameStatus) then
      logger.warn("\nNot your turn!\n")
      notifyObservers()
    else if !hexField.matrix.cell(x, y).equals(Player.Empty) then
      logger.warn("\nOccupied.")
      notifyObservers()
    else
      undoManager.redoStack = Nil
      hexField = undoManager.doStep(hexField, new PlaceCommand(hexField, c, x, y))
      lastStatus = gameStatus
      if checkStat() != GAME_OVER then gameStatus = c.other.gameStatus
      notifyObservers()

  override def undo(): Unit =
    if emptyMatrix || gameStatus == GAME_OVER then
      notifyObservers()
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

  private def checkStat(): GameStatus =
    if hexField.matrix.xCount == GAME_MAX
      || hexField.matrix.oCount == GAME_MAX
      || hexField.matrix.oCount + hexField.matrix.xCount == GAME_MAX
    then gameStatus = GAME_OVER
    else if emptyMatrix then gameStatus = IDLE
    gameStatus

  private def emptyMatrix = hexField.matrix.matrix.flatten.collect({ case Player.Empty => Player.Empty }).length == GAME_MAX

  override def reset(): Unit =
    hexField = hexField.reset
    gameStatus = IDLE
    undoManager.undoStack = Nil
    undoManager.redoStack = Nil
    notifyObservers()

  override def save(): Unit =
    fileIO.save(hexField)
    savedStatus = gameStatus
    notifyObservers()

  override def load(): Unit =
    fileIO.load match
      case Success(field) =>
        hexField = field
        gameStatus = savedStatus
        checkStat()
        notifyObservers()
      case Failure(_) => logger.error("Failed to load field")

  override def exportField: String =
    fileIO.exportGame(hexField, hexField.matrix.xCount, hexField.matrix.oCount,
      if (gameStatus.message().equals(TURN_PLAYER_2.message())) 2 else 1)
