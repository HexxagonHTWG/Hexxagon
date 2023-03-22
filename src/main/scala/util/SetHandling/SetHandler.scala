package util
package SetHandling

import scala.util.{Failure, Success, Try}

trait SetHandler:
  var toLookAt: List[Set[(Int, Int)]]

  def createSetAndHandle(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]): Seq[Vector[Char]]

  def setForEach(s: Set[(Int, Int)], matrix: Vector[Vector[Char]], content: Char): Seq[Vector[Char]] = {
    var tmpMatrix = matrix
    s.foreach {
      case (x, y) =>
        if !tmpMatrix(y)(x).equals(content)
          && !tmpMatrix(y)(x).equals(' ') then
          tmpMatrix = tmpMatrix.updated(y, tmpMatrix(y).updated(x, content))
    }
    tmpMatrix
  }
