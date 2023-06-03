package gatling

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class SpikeSpec extends GatlingBaseSpec:
  setUp(
    saveScenario("MySql").inject(
      rampUsers(lowUserAmount).during(waitDuration),
      atOnceUsers(userAmount),
      rampUsers(lowUserAmount).during(waitDuration),
    ).protocols(httpSqlBuilder),
    loadScenario("MySql").inject(
      rampUsers(lowUserAmount).during(waitDuration),
      atOnceUsers(userAmount),
      rampUsers(lowUserAmount).during(waitDuration),
    ).protocols(httpSqlBuilder),

    saveScenario("MongoDB").inject(
      rampUsers(lowUserAmount).during(waitDuration),
      atOnceUsers(userAmount),
      rampUsers(lowUserAmount).during(waitDuration),
    ).protocols(httpMongoBuilder),
    loadScenario("MongoDB").inject(
      rampUsers(lowUserAmount).during(waitDuration),
      atOnceUsers(userAmount),
      rampUsers(lowUserAmount).during(waitDuration),
    ).protocols(httpMongoBuilder),
  )
