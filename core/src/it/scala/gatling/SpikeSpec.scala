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

class SpikeSpec extends GatlingBaseSpec:

  val fieldScenario: ScenarioBuilder = scenario("Field Scenario")
    .exec(
      http("Field")
        .get("/field")
    )

  setUp(
    fieldScenario.inject(
      rampUsers(2).during(10),
      atOnceUsers(100),
      rampUsers(2).during(10),
    )
  ).protocols(httpProtocolBuilder)
