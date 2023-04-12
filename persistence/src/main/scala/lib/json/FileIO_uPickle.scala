package lib.json

import lib.{FileIOInterface, Player}
import lib.field.FieldInterface
import ujson.Obj
import upickle.default.*

import scala.io.Source

class FileIO_uPickle(using var field: FieldInterface[Player]) extends FileIOInterface:
  override def load: FieldInterface[Player] =
    val source: Source = Source.fromFile("field.json")
    val json = ujson.read(source.getLines.mkString)
    val rows = json("rows").num.toInt
    val cols = json("cols").num.toInt
    val cells = json("cells")

    for (index <- 0 until rows * cols) {
      val row = cells(index)("row").num.toInt
      val col = cells(index)("col").num.toInt
      val cell = Player.fromChar(cells(index)("cell").str.head)
      field = field.placeAlways(cell, col, row)
    }
    source.close()
    field

  override def save(field: FieldInterface[Player]): Unit =
    import java.io.*
    val pw = new PrintWriter(new File("field.json"))
    pw.write(ujson.transform(fieldToJson(field), ujson.StringRenderer(indent = 4)).toString)
    pw.close()

  // not working with xcount, ocount, turn yet
  override def exportGame(field: FieldInterface[Player], xcount: Int, ocount: Int, turn: Int): String =
    fieldToJson(field).toString

  def fieldToJson(field: FieldInterface[Player]): Obj =
    ujson.Obj(
      "rows" -> ujson.Num(field.matrix.row),
      "cols" -> ujson.Num(field.matrix.col),
      "cells" -> (
        for {
          row <- 0 until field.matrix.row
          col <- 0 until field.matrix.col
        } yield cellToJson(field, row, col)
        )
    )

  def cellToJson(field: FieldInterface[Player], row: Int, col: Int): Obj =
    ujson.Obj(
      "row" -> row,
      "col" -> col,
      "cell" -> field.matrix.cell(col, row).toString
    )
