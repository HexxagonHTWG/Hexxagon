package lib.database

import lib.GameStatus
import lib.field.FieldInterface

import scala.util.Try

/** Interface to store current game field in database. */
trait DAOInterface[T]:
  /**
   * Save current game field to database.
   * @param field the field to save
   * @return Success if the field was saved successfully, Failure otherwise
   */
  def save(field: FieldInterface[T]): Try[Unit]

  /**
   * Load game field from database.
   * @param gameId the game id to load. If None, the last game is loaded
   * @return Success with the loaded field, Failure otherwise
   */
  def load(gameId: Option[Int] = None): Try[FieldInterface[T]]
