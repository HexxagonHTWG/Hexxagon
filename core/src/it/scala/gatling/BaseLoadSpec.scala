package gatling

import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

trait BaseLoadSpec {

  protected val indexCombinations: Seq[(Int, Int)] = for {
    x <- 0 to 5
    y <- 0 to 8
  } yield (x, y)

  protected val placeScenario: ScenarioBuilder = scenario("Place Scenario")
    .exec(
      http("Reset")
        .post("/reset")
    )
    .foreach(indexCombinations, "comb", "counter") {
      exec(session =>
        val tuple = session("comb").as[(Int, Int)]
        val x = tuple._1
        val y = tuple._2
        val currentPlayer = session("counter").as[Int] % 2 match {
          case 0 => "X"
          case 1 => "O"
        }
        session.set("requestParams", s"$currentPlayer/$y/$x")
      ).exec(
        http("Place")
          .post("/place/#{requestParams}")
      )
    }

  protected val statusScenario: ScenarioBuilder = scenario("Status Scenario")
    .exec(
      http("Status")
        .get("/status")
    )
    .exec(
      http("Field")
        .get("/field")
    )

  protected val userAmount = 100
}
