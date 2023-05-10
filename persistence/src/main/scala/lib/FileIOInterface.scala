package lib

import lib.field.FieldInterface

import scala.util.Try

/** Interface to implement file IO for the current game status. */
trait FileIOInterface[T]:
  /** Loads field from a file.
   *
   * @return the loaded field
   */
  def load: Try[FieldInterface[T]]

  /** Saves field to a file.
   *
   * @param field the field to be saved
   */
  def save(field: FieldInterface[T]): Try[Unit]

  /** Returns the game as String.
   *
   * @return the game as String
   */
  def exportGame(field: FieldInterface[T], xCount: Int, oCount: Int, turn: Int): String
