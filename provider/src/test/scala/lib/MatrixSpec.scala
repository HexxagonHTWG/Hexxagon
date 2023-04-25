package lib

import lib.field.defaultImpl.Matrix
import lib.setHandling.SetHandler
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

import scala.annotation.varargs

class MatrixSpec extends AnyWordSpec:
  "A Matrix" when {
    "initialized" should {
      "contain only Player.Empty" in {
        val matrix = new Matrix(9, 6)
        matrix should be(Matrix(Vector.fill[Player](6, 9)(Player.Empty)))
        matrix.matrix.filter(_.contains(Player.Empty)) should be(Vector.fill[Player](matrix.row, matrix.col)(Player.Empty))
        matrix.matrix.filter(_.contains(Player.X)) should be(Vector())
        matrix.matrix.filter(_.contains(Player.O)) should be(Vector())
      }
      "have its counters on 0" in {
        val matrix = new Matrix(9, 6)
        matrix.xCount should be(0)
        matrix.oCount should be(0)
      }
      "use default value if given size is negative or > 10" in {
        new Matrix(10, -1) should be(new Matrix(9, 6))
        new Matrix(-1, 10) should be(new Matrix(9, 6))
        new Matrix(11, 1) should be(new Matrix(9, 6))
        new Matrix(1, 11) should be(new Matrix(9, 6))
      }
    }
    "initialized as a 9 - 6 Grid" should {
      "be the same size as an 8 - 6 Grid" in {
        new Matrix(9, 6) should be(new Matrix(8, 6))
        new Matrix(6, 6) should be(new Matrix(7, 6))
      }
    }
    "having a stone placed" should {
      var matrix = new Matrix(5, 4)
      matrix = matrix.fill(Player.O, 0, 0)
      matrix = matrix.fill(Player.X, 1, 0)
      matrix = matrix.fill(Player.O, 2, 0)
      matrix = matrix.fill(Player.Empty, 4, 3)
      "contain stone in the cell" in {
        matrix.oCount should be(2)
        matrix.matrix.flatten.count(x => x == Player.O) should be(2)
        matrix.xCount should be(1)
        matrix.matrix.flatten.count(x => x == Player.X) should be(1)
        matrix.cell(0, 0) should be(Player.X)
        matrix.cell(1, 0) should be(Player.O)
        matrix.cell(2, 0) should be(Player.O)
        matrix.cell(4, 3) should be(Player.Empty)
      }
    }
    "placing a stone on top of a stone" should {
      var matrix = new Matrix(5, 4)
      "counters should substract 1" in {
        matrix = matrix.fill(Player.O, 2, 2)
        matrix.oCount should be(1)
        matrix.xCount should be(0)
        matrix = matrix.fill(Player.X, 2, 2)
        matrix.oCount should be(0)
        matrix.xCount should be(1)
      }
    }
    "being filled with a stone" should {
      var matrix = new Matrix(5, 5)
      "only contain Player.X" in {
        matrix = matrix.fillAll(Player.X)
        matrix.xCount should be(matrix.row * matrix.col)
        matrix.oCount should be(0)
        matrix.matrix.flatten.contains(Player.Empty) should be(false)
        matrix.matrix.flatten.contains(Player.O) should be(false)
        matrix.matrix.flatten.contains(Player.X) should be(true)
      }
      "only contain Player.O" in {
        matrix = matrix.fillAll(Player.O)
        matrix.oCount should be(matrix.row * matrix.col)
        matrix.xCount should be(0)
        matrix.matrix.flatten.contains(Player.Empty) should be(false)
        matrix.matrix.flatten.contains(Player.X) should be(false)
        matrix.matrix.flatten.contains(Player.O) should be(true)
      }
      "fill results in new matrix" in {
        var mat = new Matrix(1, 1)
        mat.fillAll(Player.Empty) should be(new Matrix(1, 1))
        mat = mat.fill(Player.X, 0, 0)
        mat.cell(0, 0) should be(Player.X)
        mat = mat.fillAll(Player.Empty)
        mat.cell(0, 0) should be(Player.Empty)
      }
    }
    "used for FileIO" should {
      "fill an entry without rules applying" in {
        var m = new Matrix(9, 6)
        var m2 = new Matrix(9, 6)
        m = m.fill(Player.X, 0, 0)
        m2 = m2.fillAlways(Player.X, 0, 0)
        m should be(m2)
        m.oCount should be(m2.oCount)
        m = m.fill(Player.O, 0, 1)
        m2 = m2.fillAlways(Player.O, 0, 1)
        m should not be m2
        m.oCount should not be m2.oCount
        m2 = m2.fillAlways(Player.O, 0, 0)
        m2 should be(m)
      }
    }
  }
