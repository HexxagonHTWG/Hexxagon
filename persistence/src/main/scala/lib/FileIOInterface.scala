package lib

import lib.Player
import lib.fieldComponent.FieldInterface

/** Interface to implement file IO for the current game status. */
trait FileIOInterface:
  /** Loads field from a file.
   *
   * @return the loaded field
   */
  def load: FieldInterface[Player]

  /** Saves field to a file.
   *
   * @param field the field to be saved
   */
  def save(field: FieldInterface[Player]): Unit

  /** Returns the game status.
   *
   * @return the game status
   */
  def exportGame(field: FieldInterface[Player], xcount: Int, ocount: Int, turn: Int): String
