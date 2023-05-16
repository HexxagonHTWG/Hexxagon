package lib

import com.dimafeng.testcontainers.GenericContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.github.dockerjava.api.model.{ExposedPort, HostConfig, PortBinding, Ports}
import com.typesafe.config.ConfigFactory
import lib.database.mongoDB.DAOMongo
import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import scala.util.{Failure, Success}
import scala.jdk.CollectionConverters._

class MongoDBSpec extends AnyWordSpec:
  val container: FixedHostPortGenericContainer[Nothing] = FixedHostPortGenericContainer("mongo:latest")

  container.withEnv("MONGO_INITDB_ROOT_USERNAME", "user")
  container.withEnv("MONGO_INITDB_ROOT_PASSWORD", "root")
  container.withEnv("MONGO_INITDB_DATABASE", "hexxagon")
  container.withFixedExposedPort(27017, 27017)
  container.start()

  "The MongoDB DAO" should {
    "not be able to load a field when nothing is saved" in {
      DAOMongo.load() shouldBe a[Failure[_]]
    }
    "be able to save a field" in {
      val mockField = Field()(using new Matrix(5, 5))
      DAOMongo.save(mockField) shouldBe a[Success[_]]
    }
    "be able to load a field when something is saved" in {
      val mockField = Field()(using new Matrix(5, 5))
      DAOMongo.save(mockField) shouldBe a[Success[_]]
      DAOMongo.load() shouldBe a[Success[_]]
    }
  }
  "be able to delete a field" in {
      DAOMongo.delete(None) shouldBe a[Success[_]]
  }