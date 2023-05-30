package gatling

import com.typesafe.scalalogging.StrictLogging
import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

class LoadSpec extends Simulation with StrictLogging {

  before {
    logger.info("***** Simulation starting! *****")
  }

  after {
    logger.info("***** Simulation has ended! ******")
  }

  val theHttpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("http://localhost:9090")

  val theScenarioBuilder: ScenarioBuilder = scenario("Load Test")
    .exec(
      http("Status Request")
        .get("/status")
    )

  setUp(
    theScenarioBuilder.inject(atOnceUsers(10))
  ).protocols(theHttpProtocolBuilder)
}
