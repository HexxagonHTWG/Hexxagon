package gatling

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class StressSpec extends GatlingBaseSpec with BaseLoadSpec:
  override val userAmount = 10000
  setUp(
    placeScenario.inject(stressPeakUsers(userAmount).during(20.seconds)),
    statusScenario.inject(stressPeakUsers(userAmount).during(20.seconds))
  ).protocols(httpProtocolBuilder)
