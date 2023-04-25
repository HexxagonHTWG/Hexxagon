package lib.json

import di.ProviderModule.given_FieldInterface_Player as defaultField
import di.{FlexibleProviderModule, ProviderModule}
import lib.Player
import lib.field.FieldInterface
import play.api.libs.json.{JsNumber, JsObject, Json}

import scala.util.{Failure, Success, Try}

object HexJson:

  def encode(field: FieldInterface[Player]): String = fieldToJson(field).toString

  def fieldToJson(field: FieldInterface[Player]): JsObject =
    Json.obj(
      "rows" -> JsNumber(field.matrix.row),
      "cols" -> JsNumber(field.matrix.col),
      "cells" -> Json.toJson(
        for {
          row <- 0 until field.matrix.row
          col <- 0 until field.matrix.col
        } yield cellToJson(field, row, col)
      )
    )

  def cellToJson(field: FieldInterface[Player], row: Int, col: Int): JsObject =
    Json.obj(
      "row" -> row,
      "col" -> col,
      "cell" -> Json.toJson(field.matrix.cell(col, row).toString)
    )

  def decode(field: String): FieldInterface[Player] =
    val json = Try(Json.parse(field)) match
      case Success(value) => value
      case Failure(_) => return defaultField
    val rows = (json \ "rows").get.toString.toInt
    val cols = (json \ "cols").get.toString.toInt
    var hexField = FlexibleProviderModule(rows, cols).given_FieldInterface_Player

    for (index <- 0 until rows * cols) {
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val cell = (json \\ "cell")(index).as[String]
      hexField = hexField.placeAlways(Player.fromString(cell), col, row)
    }
    hexField
