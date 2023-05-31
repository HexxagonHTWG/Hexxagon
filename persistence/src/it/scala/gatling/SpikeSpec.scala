package gatling

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class SpikeSpec extends GatlingBaseSpec:
  setUp(
    saveScenario("MySql").inject(
      nothingFor(waitDuration),
      atOnceUsers(userAmount),
      nothingFor(waitDuration),
    ).protocols(httpSqlBuilder),
    loadScenario("MySql").inject(
      nothingFor(waitDuration),
      atOnceUsers(userAmount),
      nothingFor(waitDuration),
    ).protocols(httpSqlBuilder),

    saveScenario("MongoDB").inject(
      nothingFor(waitDuration),
      atOnceUsers(userAmount),
      nothingFor(waitDuration),
    ).protocols(httpMongoBuilder),
    loadScenario("MongoDB").inject(
      nothingFor(waitDuration),
      atOnceUsers(userAmount),
      nothingFor(waitDuration),
    ).protocols(httpMongoBuilder),
  )
