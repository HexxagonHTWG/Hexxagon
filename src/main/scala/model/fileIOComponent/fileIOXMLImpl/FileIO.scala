package model.fileIOComponent.fileIOXMLImpl

import _main_.FlexibleModule
import model.Player
import model.fieldComponent.FieldInterface
import model.fileIOComponent.FileIOInterface

import scala.xml.{Elem, NodeSeq, PrettyPrinter}

class FileIO extends FileIOInterface:
  override def load: FieldInterface[Player] =
    var field: FieldInterface[Player] = null
    val file = scala.xml.XML.loadFile("field.xml")
    val rows = file \\ "field" \ "@rows"
    val cols = file \\ "field" \ "@cols"
    field = FlexibleModule(rows.text.toInt, cols.text.toInt).given_FieldInterface_Player

    val cells = file \\ "cell"
    for (cell <- cells) {
      val r: Int = (cell \ "@row").text.trim.toInt
      val c: Int = (cell \ "@col").text.trim.toInt
      val value: Player = Player.fromString(cell.text.trim)
      field = field.placeAlways(value, c, r)
    }
    field

  override def save(field: FieldInterface[Player]): Unit =
    import java.io.*
    val pw = new PrintWriter(new File("field.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(fieldToXml(field))
    pw.write(xml)
    pw.close()

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

  def cellToXml(field: FieldInterface[Player], row: Int, col: Int): Elem =
    <cell row={row.toString} col={col.toString}>
      {field.matrix.cell(col, row)}
    </cell>
