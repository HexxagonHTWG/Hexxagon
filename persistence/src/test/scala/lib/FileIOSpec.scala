package lib

import lib.GameStatus.*
import lib.fieldComponent.fieldBaseImpl.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class FileIOSpec extends AnyWordSpec:
  "A FileIO" when {
    val matrix = new Matrix(9, 6)
    val field = new Field(using matrix)

    "used with xml" should {
      val fileIOxml = fileIOXMLImpl.FileIO(using field)
      "save current state of field in xml" in {
        val xml = fileIOxml.fieldToXml(field)
        (xml \\ "field" \ "@rows").text should be("6")
        (xml \\ "field" \ "@cols").text should be("9")
      }
      "save current state of cell in xml" in {
        val cellXml = fileIOxml.cellToXml(field, 0, 0)
        (cellXml \\ "cell" \ "@row").text should be("0")
        (cellXml \\ "cell" \ "@col").text should be("0")
        (cellXml \\ "@cell").text should be("")
      }
      "load field from xml" in {
        fileIOxml.save(field)
        fileIOxml.load should be(field)
      }
    }

    "used with json" should {
      val fileIOjson = fileIOJsonImpl.FileIO(using field)
      "save current state of field in json" in {
        val json = fileIOjson.fieldToJson(field)
        (json \ "rows").get.toString should be("6")
        (json \ "cols").get.toString should be("9")
      }
      "save current state of cell in json" in {
        val cellJson = fileIOjson.cellToJson(field, 0, 0)
        (cellJson \ "row").get.toString should be("0")
        (cellJson \ "col").get.toString should be("0")
        (cellJson \ "cell").get.toString should be("\" \"")
      }
      "load field from json" in {
        fileIOjson.save(field)
        fileIOjson.load should be(field)
      }
    }

    "used with json (uPickle)" should {
      val fileIOjson = fileIOJsonImpl.FileIO_uPickle(using field)
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
        fileIOjson.load should be(field)
      }
    }
  }
