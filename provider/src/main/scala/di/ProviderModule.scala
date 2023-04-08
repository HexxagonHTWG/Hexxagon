package di

import lib.Player
import lib.fieldComponent.fieldBaseImpl.{Field, Matrix}
import lib.fieldComponent.{FieldInterface, MatrixInterface}

class FlexibleProviderModule(rows: Int, cols: Int):
  given FieldInterface[Player] = Field()(using this.given_MatrixInterface_Player)

  given MatrixInterface[Player] = new Matrix(cols, rows)

object ProviderModule extends FlexibleProviderModule(6, 9)
