import com.dimafeng.testcontainers.scalatest.{TestContainerForAll, TestContainersForEach}
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import com.typesafe.config.ConfigFactory
import lib.Player
import lib.database.DAOInterface
import lib.database.mongoDB.DAOMongo
import lib.database.mongoDB.DAOMongo.config
import lib.database.slick.defaultImpl.DAOSlick
import lib.database.slick.jsonImpl.DAOSlick as DAOSlickJson
import lib.field.FieldInterface
import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import scala.jdk.CollectionConverters.*
import scala.language.postfixOps
import scala.util.{Failure, Success}

class DAOIntegrationSpec extends AnyWordSpec with TestContainerForAll:

  private lazy val config = ConfigFactory.load()
  private val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds

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
        Await.result(DAOSlick.load(), maxWaitSeconds) shouldBe a[Failure[_]]
      }
    }
    "something is saved" should {
      "be able to save a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        Await.result(DAOSlick.save(mockField), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to load a field" in {
        Await.result(DAOSlick.load(), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to delete a field" in {
        Await.result(DAOSlick.delete(Some(1)), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        val updatedField = mockField.place(Player.fromChar('X'), 0, 0)
        Await.result(DAOSlick.update(0, updatedField), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = Await.result(DAOSlick.load(Some(0)), maxWaitSeconds).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
  "The Slick DAO with JSON" when {
    "nothing is saved" should {
      "not be able to load a field" in {
        Await.result(DAOSlickJson.load(), maxWaitSeconds) shouldBe a[Failure[_]]
      }
    }
    "something is saved" should {
      "be able to save a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        Await.result(DAOSlickJson.save(mockField), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to load a field" in {
        Await.result(DAOSlickJson.load(), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to delete a field" in {
        Await.result(DAOSlickJson.delete(Some(1)), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        val updatedField = mockField.place(Player.fromChar('X'), 0, 0)
        DAOSlickJson.update(0, updatedField) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = Await.result(DAOSlickJson.load(Some(0)), maxWaitSeconds).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
  "The MongoDB DAO" when {
    "nothing is saved" should {
      "not be able to load a field" in {
        Await.result(DAOMongo.load(), maxWaitSeconds) shouldBe a[Failure[_]]
      }
    }
    "something is saved" should {
      "be able to save a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        Await.result(DAOMongo.save(mockField), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to load a field" in {
        Await.result(DAOMongo.load(), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to delete a field" in {
        Await.result(DAOMongo.delete(Some(1)), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to update a field" in {
        val mockField = Field()(using new Matrix(5, 5))
        val updatedField = mockField.place(Player.fromChar('X'), 0, 0)
        Await.result(DAOMongo.update(0, updatedField), maxWaitSeconds) shouldBe a[Success[_]]
      }
      "be able to load updated field" in {
        val field = Await.result(DAOMongo.load(Some(0)), scala.concurrent.duration.Duration.Inf).get
        field shouldBe a[FieldInterface[Player]]
        field.matrix.cell(0, 0) shouldBe Player.fromChar('X')
      }
    }
  }
