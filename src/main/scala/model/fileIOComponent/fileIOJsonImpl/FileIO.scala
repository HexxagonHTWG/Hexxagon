package model.fileIOComponent.fileIOJsonImpl

import model.fieldComponent.FieldInterface
import model.fileIOComponent.FileIOInterface
import play.api.libs.json.*

import scala.io.Source

class FileIO extends FileIOInterface:

  override def load: FieldInterface[Char] =
    val source: Source = Source.fromFile("field.json")
    val json: JsValue = Json.parse(source.getLines.mkString)
    val rows = (json \ "rows").get.toString.toInt
    val cols = (json \ "cols").get.toString.toInt
    var field = FlexibleModule(rows, cols).given_FieldInterface_Char

    for (index <- 0 until rows * cols) {
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val cell = (json \\ "cell")(index).as[String]
      field = field.placeAlways(cell.head, col, row)
    }
    source.close()
    field

  override def save(field: FieldInterface[Char]): Unit =
    import java.io.{File, PrintWriter}
    val pw = new PrintWriter(new File("field.json"))
    pw.write(Json.prettyPrint(fieldToJson(field)))
    pw.close()

  override def exportGame(field: FieldInterface[Char], xCount: Int, oCount: Int, turn: Int): String =
    gameToJson(field, xCount, oCount, turn).toString

  private def gameToJson(field: FieldInterface[Char], xCount: Int, oCount: Int, turn: Int): JsObject =
    Json.obj(
      "xcount" -> JsNumber(xCount),
      "ocount" -> JsNumber(oCount),
      "turn" -> JsNumber(turn),
      "field" -> fieldToJson(field)
    )

  def fieldToJson(field: FieldInterface[Char]): JsObject =
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

  def cellToJson(field: FieldInterface[Char], row: Int, col: Int): JsObject =
    Json.obj(
      "row" -> row,
      "col" -> col,
      "cell" -> Json.toJson(field.matrix.cell(col, row).toString)
    )
