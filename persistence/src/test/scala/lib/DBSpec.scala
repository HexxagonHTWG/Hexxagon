package lib

import com.typesafe.config.ConfigFactory
import lib.database.{DAOInterface, DAOMock}
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers.{be, have}
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.{a, should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Success, Try}

class DBSpec extends AnyWordSpec {
  private lazy val config = ConfigFactory.load()
  private val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds

  "A Database Interface" when {
    val mockField = Field()(using new Matrix(5, 5))
    "defined" should {
      "have the necessary crud operations" in {
        class AnyDAO extends DAOInterface[Any]:
          override def save(field: FieldInterface[Any]): Future[Try[Unit]] = Future(Try(()))

          override def delete(id: Option[Int]): Future[Try[Unit]] = Future(Try(()))

          override def load(id: Option[Int]): Future[Try[FieldInterface[Any]]] = 
            Future(Try(mockField.asInstanceOf[FieldInterface[Any]]))

          override def update(id: Int, field: FieldInterface[Any]): Future[Try[Unit]] = Future(Try(()))
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
        Await.result(DAOMock.save(mockField), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "contain a method to delete game" in {
        Await.result(DAOMock.delete(None), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "contain a method to load game" in {
        Await.result(DAOMock.load(None), maxWaitSeconds) shouldBe a[Success[FieldInterface[_]]]
      }
      "contain a method to update game" in {
        Await.result(DAOMock.update(0, mockField), maxWaitSeconds) shouldBe a[Success[_]]
      }
    }
  }
}
