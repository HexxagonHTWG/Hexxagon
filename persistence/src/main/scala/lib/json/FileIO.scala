package lib.json

import lib.field.FieldInterface
import lib.{FileIOInterface, Player}
import play.api.libs.json.*

import scala.io.Source

case class FileIO() extends FileIOInterface[Player]:
  private val fileName = "field.json"

  override def load: FieldInterface[Player] =
    val source: Source = Source.fromFile(fileName)
    HexJson.decode(source.getLines.mkString)

  override def save(field: FieldInterface[Player]): Unit =
    import java.io.{File, PrintWriter}
    val pw = new PrintWriter(new File(fileName))
    pw.write(Json.prettyPrint(HexJson.fieldToJson(field)))
    pw.close()

  override def exportGame(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): String =
    gameToJson(field, xCount, oCount, turn).toString

  private def gameToJson(field: FieldInterface[Player], xCount: Int, oCount: Int, turn: Int): JsObject =
    Json.obj(
      "x-count" -> JsNumber(xCount),
      "o-count" -> JsNumber(oCount),
      "turn" -> JsNumber(turn),
      "field" -> HexJson.fieldToJson(field)
    )
