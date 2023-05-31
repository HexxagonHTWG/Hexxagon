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
import org.testcontainers.containers.wait.strategy.Wait
import lib.json.HexJson

import java.io.File
import scala.concurrent.duration.*
import scala.util.{Failure, Success, Try}

class LoadSpec extends Simulation with StrictLogging {

  private lazy val config = ConfigFactory.load()
  private lazy val fallBackUrl = "http://0.0.0.0:8081"
  private lazy val portSql = config.getString("http.test.sql.port")
  private lazy val portMongo = config.getString("http.test.mongo.port")
  private lazy val persistenceSqlUrl =
    Try(s"http://${config.getString("http.test.sql.host")}:$portSql") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl
  private lazy val persistenceMongoUrl =
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

  val httpSqlBuilder: HttpProtocolBuilder = http
    .baseUrl(persistenceSqlUrl)
  val httpMongoBuilder: HttpProtocolBuilder = http
    .baseUrl(persistenceMongoUrl)

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

  var container: DockerComposeContainer = _
  before {
    container = testContainer.start()
  }
  after {
    container.stop()
  }

  setUp(
    saveScenario("MySql").inject(rampUsers(500).during(20.seconds)).protocols(httpSqlBuilder),
    loadScenario("MySql").inject(rampUsers(500).during(20.seconds)).protocols(httpSqlBuilder),

    saveScenario("MongoDB").inject(rampUsers(500).during(20.seconds)).protocols(httpMongoBuilder),
    loadScenario("MongoDB").inject(rampUsers(500).during(20.seconds)).protocols(httpMongoBuilder),
  )
}
