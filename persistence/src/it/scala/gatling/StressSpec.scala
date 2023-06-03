package gatling

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class StressSpec extends GatlingBaseSpec:
  override protected val userAmount: Int = 500
  setUp(
    saveScenario("MySql").inject(stressPeakUsers(userAmount).during(fullDuration)).protocols(httpSqlBuilder),
    loadScenario("MySql").inject(stressPeakUsers(userAmount).during(fullDuration)).protocols(httpSqlBuilder),

    saveScenario("MongoDB").inject(stressPeakUsers(userAmount).during(fullDuration)).protocols(httpMongoBuilder),
    loadScenario("MongoDB").inject(stressPeakUsers(userAmount).during(fullDuration)).protocols(httpMongoBuilder),
  )
