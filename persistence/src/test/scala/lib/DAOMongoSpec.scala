package lib

import lib.database.mongoDB.DAOMongo
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}
import lib.Player
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.containers.FixedHostPortGenericContainer

import scala.util.{Failure, Success}
import scala.jdk.CollectionConverters.*

class DAOMongoSpec extends AnyWordSpec:
  val container: FixedHostPortGenericContainer[Nothing] = 
    FixedHostPortGenericContainer("mongo:latest")
  container.withFixedExposedPort(27017, 27017)
  container.withEnv(Map(
    "MONGO_INITDB_ROOT_USERNAME" -> "user", 
    "MONGO_INITDB_ROOT_PASSWORD" -> "root",
    "MONGO_INITDB_DATABASE" -> "hexxagon").asJava)
  container.start()

  "The MongoDB DAO" when {
    "nothing is saved" should {
      "not be able to load a field" in {
        DAOMongo.load() shouldBe a[Failure[_]]
      }
    }
    "something is saved" should {
      "be able to save a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        DAOMongo.save(mockField) shouldBe a[Success[_]]
      }
      "be able to load a field" in {
        DAOMongo.load() shouldBe a[Success[_]]
      }
      "be able to delete a field" in {
        DAOMongo.delete(None) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        mockField.place(Player.fromChar('X'), 0, 0)
        DAOMongo.update(0, mockField) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = DAOMongo.load(Some(0)).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
