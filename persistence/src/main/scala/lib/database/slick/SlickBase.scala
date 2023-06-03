package lib.database.slick

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*

import scala.concurrent.duration.{Duration, DurationInt, SECONDS}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.control.Breaks.{break, breakable}
import scala.util.{Failure, Success, Try}

trait SlickBase extends StrictLogging:

  protected lazy val config: Config = ConfigFactory.load()
  protected val databaseUrl: String =
    s"jdbc:${config.getString("db.slick.protocol")}://" +
      s"${config.getString("db.host")}:" +
      s"${config.getString("db.slick.port")}/" +
      s"${config.getString("db.name")}?serverTimezone=CET"

  logger.debug(s"Database URL: $databaseUrl")
  protected val database = Database.forURL(
    url = databaseUrl,
    driver = config.getString("db.slick.driver"),
    user = config.getString("db.user"),
    password = config.getString("db.password")
  )

  protected val maxWaitSeconds: Duration = config.getInt("db.maxWaitSeconds") seconds
  protected val maxGameCount: Int = config.getInt("db.maxGameCount")
  private val connectionRetryAttempts: Int = config.getInt("db.connectionRetryAttempts")
  protected var gameIdCounter = 0

  protected def init(setup: DBIOAction[Unit, NoStream, Effect.Schema]): Unit =
    logger.info("Connecting to DB...")
    breakable {
      for (i <- 1 to connectionRetryAttempts)
        Try(Await.result(database.run(setup), maxWaitSeconds)) match
          case Success(_) => logger.info("DB connection established"); break
          case Failure(e) =>
            if e.getMessage.contains("Multiple primary key defined") then // ugly workaround: https://github.com/slick/slick/issues/1999
              logger.info("Assuming DB connection established")
              break
            logger.info(s"DB connection failed - retrying... - $i/$connectionRetryAttempts")
            logger.warn(e.getMessage)
            Thread.sleep(maxWaitSeconds.toMillis)
    }
