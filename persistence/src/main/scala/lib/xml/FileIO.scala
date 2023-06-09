package lib.xml

import lib.field.FieldInterface
import lib.{FileIOInterface, Player}

import scala.util.{Success, Try}
import scala.xml.{Elem, NodeSeq, PrettyPrinter}

class FileIO(using var field: FieldInterface[Player]) extends FileIOInterface[Player]:
  private val fileName = "field.xml"

  override def load: Try[FieldInterface[Player]] =
    Try {
      val file = scala.xml.XML.loadFile(fileName)
      val cells = file \\ "cell"
      for (cell <- cells) {
        val r: Int = (cell \ "@row").text.trim.toInt
        val c: Int = (cell \ "@col").text.trim.toInt
        val value: Player = Player.fromString(cell.text.trim)
        field = field.placeAlways(value, c, r)
      }
      field
    }

  override def save(field: FieldInterface[Player]): Try[Unit] =
    Try {
      import java.io.*
      val pw = new PrintWriter(new File(fileName))
      val prettyPrinter = new PrettyPrinter(120, 4)
      val xml = prettyPrinter.format(fieldToXml(field))
      pw.write(xml)
      pw.close()
      Success(())
    }

  def fieldToXml(field: FieldInterface[Player]): Elem =
    <field rows={field.matrix.row.toString} cols={field.matrix.col.toString}>
      {for {
      l <- 0 until field.matrix.row
      i <- 0 until field.matrix.col
    } yield cellToXml(field, l, i)}
    </field>

  override def exportGame(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): String =
    gameToXml(field, xCount, oCount, turn).toString

  private def gameToXml(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int) =
    <field rows={field.matrix.row.toString} cols={field.matrix.col.toString}>
      {<x-count>
      {xCount}
    </x-count>
      <o-count>
        {oCount}
      </o-count>
      <turn>
        {turn}
      </turn>}{for {
      l <- 0 until field.matrix.row
      i <- 0 until field.matrix.col
    } yield cellToXml(field, l, i)}
    </field>

  def cellToXml(field: FieldInterface[Player], row: Int, col: Int): Elem =
    <cell row={row.toString} col={col.toString}>
      {field.matrix.cell(col, row)}
    </cell>
