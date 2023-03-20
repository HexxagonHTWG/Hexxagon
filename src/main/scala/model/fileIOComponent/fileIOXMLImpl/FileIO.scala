package model.fileIOComponent.fileIOXMLImpl

import model.fieldComponent.FieldInterface
import model.fileIOComponent.FileIOInterface

import scala.xml.{Elem, NodeSeq, PrettyPrinter}

class FileIO extends FileIOInterface:
  override def load: FieldInterface[Char] =
    var field: FieldInterface[Char] = null
    val file = scala.xml.XML.loadFile("field.xml")
    val rows = file \\ "field" \ "@rows"
    val cols = file \\ "field" \ "@cols"
    field = FlexibleModule(rows.text.toInt, cols.text.toInt).given_FieldInterface_Char

    val cells = file \\ "cell"
    for (cell <- cells) {
      val r: Int = (cell \ "@row").text.trim.toInt
      val c: Int = (cell \ "@col").text.trim.toInt
      val value: Char = {
        cell.text.trim match {
          case "" => ' '
          case "X" => 'X'
          case "O" => 'O'
        }
      }
      field = field.placeAlways(value, c, r)
    }
    field

  override def save(field: FieldInterface[Char]): Unit =
    import java.io.*
    val pw = new PrintWriter(new File("field.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(fieldToXml(field))
    pw.write(xml)
    pw.close()

  def fieldToXml(field: FieldInterface[Char]): Elem =
    <field rows={field.matrix.row.toString} cols={field.matrix.col.toString}>
      {for {
      l <- 0 until field.matrix.row
      i <- 0 until field.matrix.col
    } yield cellToXml(field, l, i)}
    </field>

  override def exportGame(field: FieldInterface[Char], xCount: Int, oCount: Int, turn: Int): String =
    gameToXml(field, xCount, oCount, turn).toString

  private def gameToXml(field: FieldInterface[Char], xCount: Int, oCount: Int, turn: Int) =
    <field rows={field.matrix.row.toString} cols={field.matrix.col.toString}>
      {<xcount>
      {xCount}
    </xcount>
      <ocount>
        {oCount}
      </ocount>
      <turn>
        {turn}
      </turn>}{for {
      l <- 0 until field.matrix.row
      i <- 0 until field.matrix.col
    } yield cellToXml(field, l, i)}
    </field>

  def cellToXml(field: FieldInterface[Char], row: Int, col: Int): Elem =
    <cell row={row.toString} col={col.toString}>
      {field.matrix.cell(col, row)}
    </cell>
