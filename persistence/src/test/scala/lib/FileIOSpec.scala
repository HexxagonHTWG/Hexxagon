package lib

import lib.GameStatus.*
import lib.field.defaultImpl.*
import lib.json.HexJson
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.JsObject

class FileIOSpec extends AnyWordSpec with PrivateMethodTester:
  "A FileIO" when {
    val matrix = new Matrix(9, 6)
    val field = new Field(using matrix)

    "used with xml" should {
      val fileIOXml = xml.FileIO(using field)
      "save current state of field in xml" in {
        val xml = fileIOXml.fieldToXml(field)
        (xml \\ "field" \ "@rows").text should be("6")
        (xml \\ "field" \ "@cols").text should be("9")
      }
      "save current state of cell in xml" in {
        val cellXml = fileIOXml.cellToXml(field, 0, 0)
        (cellXml \\ "cell" \ "@row").text should be("0")
        (cellXml \\ "cell" \ "@col").text should be("0")
        (cellXml \\ "@cell").text should be("")
      }
      "load field from xml" in {
        fileIOXml.save(field)
        fileIOXml.load.get should be(field)
      }
      "export a field" in {
        val gameToXml = PrivateMethod[scala.xml.Elem](Symbol("gameToXml"))
        val s = fileIOXml invokePrivate gameToXml(field, 0, 0, 0)
        fileIOXml.exportGame(field, 0, 0, 0) should be(s.toString)
      }
    }

    "used with json" should {
      val fileIOjson = json.FileIO()
      "save current state of field in json" in {
        val json = HexJson.fieldToJson(field)
        (json \ "rows").get.toString should be("6")
        (json \ "cols").get.toString should be("9")
      }
      "save current state of cell in json" in {
        val cellJson = HexJson.cellToJson(field, 0, 0)
        (cellJson \ "row").get.toString should be("0")
        (cellJson \ "col").get.toString should be("0")
        (cellJson \ "cell").get.toString should be("\" \"")
      }
      "load field from json" in {
        fileIOjson.save(field)
        fileIOjson.load.get should be(field)
      }
      "export a field" in {
        val gameToJson = PrivateMethod[JsObject](Symbol("gameToJson"))
        val s = fileIOjson invokePrivate gameToJson(field, 0, 0, 0)
        fileIOjson.exportGame(field, 0, 0, 0) should be(s.toString)
      }
    }

    "used with json (uPickle)" should {
      val fileIOjson = json.FileIO_uPickle(using field)
      "save current state of field in json" in {
        val json = fileIOjson.fieldToJson(field)
        json("rows").num.toInt should be(6)
        json("cols").num.toInt should be(9)
      }
      "save current state of cell in json" in {
        val cellJson = fileIOjson.cellToJson(field, 0, 0)
        cellJson("row").num.toInt should be(0)
        cellJson("col").num.toInt should be(0)
        cellJson("cell").toString should be("\" \"")
      }
      "load field from json" in {
        fileIOjson.save(field)
        fileIOjson.load.get should be(field)
      }
    }
  }
