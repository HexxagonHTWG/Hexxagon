package lib.database.slick

import lib.Player
import lib.database.DAOInterface
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}

import scala.util.{Success, Try}

case object DAOSlickMock extends DAOInterface[Player]:
  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      Success(())
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      Success(())
    }

  override def delete(gameId: Option[Int]): Try[Unit] =
    Try {
      Success(())
    }

  override def load(gameId: Option[Int]): Try[FieldInterface[Player]] =
    Try {
      Field()(using new Matrix(5, 5))
    }

  private def insertField(gameId: Int, field: FieldInterface[Player]): Try[Unit] =
    Try {
      Success(())
    }
