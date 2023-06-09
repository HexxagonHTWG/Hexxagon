package lib

import lib.field.defaultImpl.{Field, Matrix}
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class FieldSpec extends AnyWordSpec:
  info("Note that Fields have to have an uneven number of columns!")
  "A Hex" when {
    "created as 9 - 6 Grid" should {
      val hex = new Field(using new Matrix(9, 6))

      "start with the top" in {
        hex.edgeTop should be(new Field(using new Matrix(hex.matrix.col, 1)).edgeTop)
      }
      "have lines" in {
        "\n" + hex.edgeTop + hex.top(0) + hex.bot(0) + hex.edgeBot should be(new Field(using new Matrix(hex.matrix.col, 1)).field)
      }
      "be equal to a field the same size" in {
        hex.field should be(new Field(using new Matrix(hex.matrix.col, hex.matrix.row)).field)
      }
      "placing at 8 5 X" in {
        hex.place(Player.X, 8, 5).matrix should be(new Matrix(9, 6).fill(Player.X, 8, 5))
      }
      "be the same size as an 8 - 6 Grid" in {
        new Field(using new Matrix(9, 6)).field should be(new Field(using new Matrix(8, 6)).field)
      }
      "be filled with one method call" in {
        hex.fillAll(Player.X).matrix should be(new Matrix(9, 6).fillAll(Player.X))
      }
    }

    "created as default Grid" should {
      val hex = new Field(using new Matrix(9, 6))

      "start with the top" in {
        hex.edgeTop should be(new Field(using new Matrix(hex.matrix.col, 1)).edgeTop)
      }
      "have lines" in {
        "\n" + hex.edgeTop + hex.top(0) + hex.bot(0) + hex.edgeBot should be(new Field(using new Matrix(hex.matrix.col, 1)).field)
      }
      "be equal to a field with the size: 9 - 6" in {
        hex.field should be(new Field(using new Matrix(9, 6)).field)
      }
      "be empty in every Cell at the beginning" in {
        hex.matrix.matrix.contains(Player.X) should be(false)
        hex.matrix.matrix.contains(Player.O) should be(false)
        val nhex = new Field(using new Matrix(2, 3))
        nhex.matrix.matrix.contains(Player.X) should be(false)
        nhex.matrix.matrix.contains(Player.O) should be(false)
      }
    }
    "created as Single Cell" should {

      var field = new Field(using new Matrix(1, 1))

      "contain a Space when empty" in {
        field.matrix.matrix(0)(0) should be(Player.Empty)
      }
      "contain a X" in {
        field = field.place(Player.X, 0, 0).asInstanceOf[Field]
        field.matrix.matrix(0)(0) should be(Player.X)
      }
      "contain a O" in {
        new Field(using new Matrix(1, 1))
        field = field.place(Player.O, 0, 0).asInstanceOf[Field]
        field.matrix.matrix(0)(0) should be(Player.O)
      }
      "be filled completely" in {
        field = field.fillAll(Player.X).asInstanceOf[Field]
        field.matrix.matrix.flatten should contain(Player.X)
        field.matrix.matrix.flatten should not contain Player.Empty
        field.matrix.matrix.flatten should not contain Player.O
      }
    }
    "handling FileIO" should {
      "able to place stones without rules applying" in {
        var hex = new Field(using new Matrix(9, 6))
        var hex2 = new Field(using new Matrix(9, 6))
        hex = hex.place(Player.X, 0, 0).asInstanceOf[Field]
        hex2 = hex2.placeAlways(Player.X, 0, 0).asInstanceOf[Field]
        hex.toString should be(hex2.toString)
        hex.place(Player.O, 0, 1).toString should not be hex2.placeAlways(Player.O, 0, 1).toString
      }
    }
  }
