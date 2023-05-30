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

class LoadSpec extends Simulation with StrictLogging {

  private lazy val config = ConfigFactory.load()
  private lazy val fallBackUrl = "http://0.0.0.0:8080"
  private lazy val coreUrl =
    Try(s"http://${config.getString("http.core.host")}:${config.getString("http.core.port")}") match
      case Success(value) => value
      case Failure(exception) => logger.error(s"${exception.getMessage} - Using fallback url: $fallBackUrl"); fallBackUrl

  val testContainer: DockerComposeContainer.Def =
    DockerComposeContainer.Def(
      new File("integration.yml"),
      exposedServices = Seq(
        ExposedService("core", 9090, Wait.forListeningPort()),
      )
    )
  before {
    testContainer.start()
    testContainer.wait(30000)
  }

  after {

  }

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl(coreUrl)

  val indexCombinations: Seq[(Int, Int)] = for {
    x <- 0 to 5
    y <- 0 to 8
  } yield (x, y)

  val placeScenario: ScenarioBuilder = scenario("Place Scenario")
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
          .post("/place/${requestParams}")
      )
    }

  setUp(
    placeScenario.inject(rampUsers(1000).during(20.seconds))
  ).protocols(httpProtocolBuilder)
}
