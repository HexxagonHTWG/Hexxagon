package lib

import lib.database.{DAOInterface, DAOMock}
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers.{be, have}
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.{a, should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{Success, Try}

class DBSpec extends AnyWordSpec {
  "A Database Interface" when {
    val mockField = Field()(using new Matrix(5, 5))
    "defined" should {
      "have the necessary crud operations" in {
        class AnyDAO extends DAOInterface[Any]:
          override def save(field: FieldInterface[Any]): Try[Unit] = Try(())

          override def delete(id: Option[Int]): Try[Unit] = Try(())

          override def load(id: Option[Int]): Try[FieldInterface[Any]] = Try(mockField.asInstanceOf[FieldInterface[Any]])

          override def update(id: Int, field: FieldInterface[Any]): Try[Unit] = Try(())
        end AnyDAO
        val anyDAO = new AnyDAO
        anyDAO shouldBe a[DAOInterface[_]]
      }
    }
    "implemented" should {
      "be a DAOInterface" in {
        DAOMock shouldBe a[DAOInterface[_]]
      }
      "contain a method to insert a new game" in {
        DAOMock.save(mockField) shouldBe a[Success[_]]
      }
      "contain a method to delete game" in {
        DAOMock.delete(None) shouldBe a[Success[_]]
      }
      "contain a method to load game" in {
        DAOMock.load(None) shouldBe a[Success[FieldInterface[_]]]
      }
      "contain a method to update game" in {
        DAOMock.update(0, mockField) shouldBe a[Success[_]]
      }
    }
  }
}
