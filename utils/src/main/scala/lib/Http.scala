package lib

import com.typesafe.scalalogging.StrictLogging
import requests.{Requester, Response}

import scala.util.{Failure, Success, Try}

object Http extends StrictLogging:

  private val loggingFormat = s"%-5s %-32s -> %.20s"

  def fetch(method: Requester, url: String): String =
    reqLogger(Try(method(url)), method.verb)

  def fetch(method: Requester, url: String, body: String): String =
    reqLogger(Try(method(url, data = body)), method.verb)

  private def reqLogger(t: Try[Response], verb: String): String =
    t match
      case Success(response) =>
        val r = response.text()
        logger.debug(
          String.format(loggingFormat,
            verb, response.url, r
          )
        )
        r
      case Failure(exception) =>
        logger.error(exception.getMessage)
        ""
