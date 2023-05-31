package gatling

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class LoadSpec extends GatlingBaseSpec with BaseLoadSpec {
  setUp(
    placeScenario.inject(rampUsers(userAmount).during(20.seconds)),
    statusScenario.inject(rampUsers(userAmount).during(20.seconds))
  ).protocols(httpProtocolBuilder)
}
