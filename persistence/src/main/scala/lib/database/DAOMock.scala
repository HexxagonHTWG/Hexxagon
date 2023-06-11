package lib.database

import lib.Player
import lib.database.DAOInterface
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Try}

case object DAOMock extends DAOInterface[Player]:
  override def save(field: FieldInterface[Player]): Future[Try[Unit]] =
    Future {
      Success(())
    }

  override def update(gameId: Int, field: FieldInterface[Player]): Future[Try[Unit]] =
    Future {
      Success(())
    }

  override def delete(gameId: Option[Int]): Future[Try[Unit]] =
    Future {
      Success(())
    }

  override def load(gameId: Option[Int]): Future[Try[FieldInterface[Player]]] =
    Future {
      Success(Field()(using new Matrix(5, 5)))
    }
