package model

import controller.GameStatus.*
import controller.controllerComponent.controllerBaseImpl.Controller
import model.fieldComponent.fieldBaseImpl.{Field, Matrix}
import model.fileIOComponent.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class FileIOSpec extends AnyWordSpec:
  "A FileIO" when {
    val matrix = new Matrix(9, 6)
    val field = new Field(using matrix)

    "used with xml" should {
      val fileIOxml = fileIOXMLImpl.FileIO()
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
      val fileIOjson = fileIOJsonImpl.FileIO()
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
      val fileIOjson = fileIOJsonImpl.FileIO_uPickle()
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

    "used by controller" should {
      "save and load" in {
        val c = HexModule.given_ControllerInterface_Char
        c.place('X', 0, 0)
        c.save()
        val hex = c.hexField
        c.place('O', 1, 0)
        c.hexField.toString should not be hex.toString
        c.load()
        c.hexField.toString should be(hex.toString)
      }

      "keep the saved gamestatus after loading" in {
        val c = FlexibleModule(7, 4).given_ControllerInterface_Char
        c.gameStatus should be(IDLE)
        c.place('X', 0, 0)
        c.gameStatus should be(TURN_PLAYER_2)
        c.save()
        c.place('O', 3, 3)
        c.gameStatus should be(TURN_PLAYER_1)
        c.load()
        c.gameStatus should be(TURN_PLAYER_2)
      }
    }
  }
