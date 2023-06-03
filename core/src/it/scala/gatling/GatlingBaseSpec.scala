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

import java.io.File
import scala.concurrent.duration.*
import scala.util.{Failure, Success, Try}

trait GatlingBaseSpec extends Simulation with StrictLogging:

  protected lazy val config: Config = ConfigFactory.load()
  protected lazy val fallBackUrl = "http://0.0.0.0:8080"
  protected lazy val port: String = config.getString("http.test.port")
  protected lazy val coreUrl: String =
    Try(s"http://${config.getString("http.test.host")}:$port") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl

  val testContainer: DockerComposeContainer.Def =
    DockerComposeContainer.Def(
      new File("integration.yml"),
      exposedServices = Seq(
        ExposedService("core", port.toInt, Wait.forListeningPort()),
      )
    )

  var container: DockerComposeContainer = _
  before {
    container = testContainer.start()
  }
  after {
    container.stop()
  }

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl(coreUrl)
