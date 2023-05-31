package gatling

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class LoadSpec extends GatlingBaseSpec:
  setUp(
    saveScenario("MySql").inject(rampUsers(userAmount).during(fullDuration)).protocols(httpSqlBuilder),
    loadScenario("MySql").inject(rampUsers(userAmount).during(fullDuration)).protocols(httpSqlBuilder),

    saveScenario("MongoDB").inject(rampUsers(userAmount).during(fullDuration)).protocols(httpMongoBuilder),
    loadScenario("MongoDB").inject(rampUsers(userAmount).during(fullDuration)).protocols(httpMongoBuilder),
  )
