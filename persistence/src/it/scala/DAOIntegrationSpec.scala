import com.dimafeng.testcontainers.scalatest.{TestContainerForAll, TestContainersForEach}
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import com.typesafe.config.ConfigFactory
import lib.Player
import lib.database.DAOInterface
import lib.database.mongoDB.DAOMongo
import lib.database.slick.defaultImpl.DAOSlick
import lib.database.slick.jsonImpl.DAOSlick as DAOSlickJson
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success}

class DAOIntegrationSpec extends AnyWordSpec with TestContainerForAll:

  private lazy val config = ConfigFactory.load()

  override val containerDef: ContainerDef = DockerComposeContainer.Def(
    new File("db-integration-test.yml"),
    exposedServices = Seq(
      ExposedService("db-sql", 3306, Wait.forListeningPort()),
      ExposedService("db-mongo", 27017, Wait.forListeningPort())
    )
  )

  "The Slick DAO" when {
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
      "be able to delete a field" in {
        DAOSlick.delete(Some(1)) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        val updatedField = mockField.place(Player.fromChar('X'), 0, 0)
        DAOSlick.update(0, updatedField) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = DAOSlick.load(Some(0)).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
  "The Slick DAO with JSON" when {
    "nothing is saved" should {
      "not be able to load a field" in {
        DAOSlickJson.load() shouldBe a[Failure[_]]
      }
    }
    "something is saved" should {
      "be able to save a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        DAOSlickJson.save(mockField) shouldBe a[Success[_]]
      }
      "be able to load a field" in {
        DAOSlickJson.load() shouldBe a[Success[_]]
      }
      "be able to delete a field" in {
        DAOSlickJson.delete(Some(1)) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        val updatedField = mockField.place(Player.fromChar('X'), 0, 0)
        DAOSlickJson.update(0, updatedField) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = DAOSlickJson.load(Some(0)).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
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
        DAOMongo.delete(Some(1)) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        val updatedField = mockField.place(Player.fromChar('X'), 0, 0)
        DAOMongo.update(0, updatedField) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = DAOMongo.load(Some(0)).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
