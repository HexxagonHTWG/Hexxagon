package lib

import lib.database.slick.DAOSlick
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.containers.FixedHostPortGenericContainer

import scala.util.{Failure, Success}
import scala.jdk.CollectionConverters.*

class DAOSlickSpec extends AnyWordSpec:
  val container: FixedHostPortGenericContainer[Nothing] =
    FixedHostPortGenericContainer("mysql:latest")
  container.withFixedExposedPort(3306, 3306)
  container.withEnv(Map(
    "MYSQL_ROOT_PASSWORD" -> "root",
    "MYSQL_PASSWORD" -> "root",
    "MYSQL_USER" -> "user",
    "MYSQL_DATABASE" -> "hexxagon").asJava)
  container.start()

  "The MongoDB DAO" when {
    "nothing is saved" should {
      "not be able to load a field" in {
        DAOSlick.load() shouldBe a[Failure[_]]
      }
    }
    "something is saved" should {
      "be able to save a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        DAOSlick.save(mockField) shouldBe a[Success[_]]
      }
      "be able to load a field" in {
        DAOSlick.load() shouldBe a[Success[_]]
      }
      /*"be able to delete a field" in {
        DAOSlick.delete(None) shouldBe a[Success[_]]
      }*/
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        mockField.place(Player.fromChar('X'), 0, 0)
        DAOSlick.update(0, mockField) shouldBe a[Success[_]]
      }
      /*"be able to load updated field" in {
        val field = DAOSlick.load(Some(0)).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }*/
    }
  }
