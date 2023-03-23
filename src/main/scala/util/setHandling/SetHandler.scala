package util
package setHandling

import scala.util.{Failure, Success, Try}

trait SetHandler:

  def handle(): Seq[Vector[Char]]

  final def setForEach(s: Set[(Int, Int)], matrix: Vector[Vector[Char]], content: Char): Seq[Vector[Char]] = {
    var tmpMatrix = matrix
    s.foreach {
      (x, y) =>
        if !tmpMatrix(y)(x).equals(content)
          && !tmpMatrix(y)(x).equals(' ') then
          tmpMatrix = tmpMatrix.updated(y, tmpMatrix(y).updated(x, content))
    }
    tmpMatrix
  }

  protected def toLookAt: List[Set[(Int, Int)]]
