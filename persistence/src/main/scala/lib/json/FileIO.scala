package lib.json

import lib.field.FieldInterface
import lib.{FileIOInterface, Player}
import play.api.libs.json.*

import scala.io.Source

class FileIO(using var field: FieldInterface[Player]) extends FileIOInterface:

  override def load: FieldInterface[Player] =
    val source: Source = Source.fromFile("field.json")
    val json: JsValue = Json.parse(source.getLines.mkString)
    val rows = (json \ "rows").get.toString.toInt
    val cols = (json \ "cols").get.toString.toInt

    for (index <- 0 until rows * cols) {
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val cell = (json \\ "cell")(index).as[String]
      field = field.placeAlways(Player.fromString(cell), col, row)
    }
    source.close()
    field

  override def save(field: FieldInterface[Player]): Unit =
    import java.io.{File, PrintWriter}
    val pw = new PrintWriter(new File("field.json"))
    pw.write(Json.prettyPrint(HexJson.fieldToJson(field)))
    pw.close()

  override def exportGame(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): String =
    gameToJson(field, xCount, oCount, turn).toString

  private def gameToJson(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): JsObject =
    Json.obj(
      "xcount" -> JsNumber(xCount),
      "ocount" -> JsNumber(oCount),
      "turn" -> JsNumber(turn),
      "field" -> HexJson.fieldToJson(field)
    )
