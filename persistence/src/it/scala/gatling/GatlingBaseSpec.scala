package gatling

import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging
import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.jdbc.Predef.*
import lib.json.HexJson
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File
import scala.concurrent.duration.*
import scala.util.{Failure, Success, Try}

trait GatlingBaseSpec extends Simulation with StrictLogging:

  protected lazy val config: Config = ConfigFactory.load()
  protected lazy val fallBackUrl = "http://0.0.0.0:8081"
  protected lazy val portSql: String = config.getString("http.test.sql.port")
  protected lazy val portMongo: String = config.getString("http.test.mongo.port")
  protected lazy val persistenceSqlUrl: String =
    Try(s"http://${config.getString("http.test.sql.host")}:$portSql") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl
  protected lazy val persistenceMongoUrl: String =
    Try(s"http://${config.getString("http.test.mongo.host")}:$portMongo") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl

  val testContainer: DockerComposeContainer.Def =
    DockerComposeContainer.Def(
      new File("db-integration-test.yml"),
      exposedServices = Seq(
        ExposedService("db-mongo", 27017, Wait.forListeningPort()),
        ExposedService("db-sql", 3306, Wait.forListeningPort()),
        ExposedService("persistence-mongo", portMongo.toInt, Wait.forListeningPort()),
        ExposedService("persistence-sql", portSql.toInt, Wait.forListeningPort()),
      )
    )

  var container: DockerComposeContainer = _
  before {
    container = testContainer.start()
  }
  after {
    container.stop()
  }

  def saveScenario(impl: String): ScenarioBuilder = scenario(s"$impl - Save Scenario")
    .exec(
      http(s"$impl - Save")
        .post("/save")
        .body(RawFileBody("it-field.json"))
    )

  def loadScenario(impl: String): ScenarioBuilder = scenario(s"$impl - Load Scenario")
    .exec(
      http(s"$impl - Load")
        .get("/load")
    )

  val httpSqlBuilder: HttpProtocolBuilder = http
    .baseUrl(persistenceSqlUrl)
  val httpMongoBuilder: HttpProtocolBuilder = http
    .baseUrl(persistenceMongoUrl)

  protected val fullDuration: FiniteDuration = 20.seconds
  protected val waitDuration: FiniteDuration = 10.seconds
  protected val userAmount = 100
  protected val lowUserAmount = 2
