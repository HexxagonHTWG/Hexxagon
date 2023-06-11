package lib.database

import lib.GameStatus
import lib.field.FieldInterface

import scala.concurrent.Future
import scala.util.Try

/** Interface to store current game field in database. */
trait DAOInterface[T]:
  /**
   * Save current game field to database.
   * @param field the field to save
   * @return Success if the field was saved successfully, Failure otherwise
   */
  def save(field: FieldInterface[T]): Future[Try[Unit]]

  /**
   * Load game field from database.
   * @param gameId the game id to load. If None, the last game is loaded
   * @return Success with the loaded field, Failure otherwise
   */
  def load(gameId: Option[Int] = None): Future[Try[FieldInterface[T]]]

  /**
   * Update game field in database.
   * @param gameId the game id to update
   * @param field the field to save instead
   * @return Success if the game was updated successfully, Failure otherwise
   */
  def update(gameId: Int, field: FieldInterface[T]): Future[Try[Unit]]

  /**
   * Delete game field from database.
   * @param gameId the game id to delete. If None, the last game is deleted
   * @return Success if the game was deleted successfully, Failure otherwise
   */
  def delete(gameId: Option[Int]): Future[Try[Unit]]
