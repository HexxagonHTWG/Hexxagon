package util
package setHandling

import scala.util.{Failure, Success, Try}

case class CornerSetHandler(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]) extends SideSetHandler(content, x, y, startmatrix):

  override def nextHandler: Seq[Vector[Char]] = startmatrix

  override def coordinates: List[Set[(Int, Int)]] = List(
    Set((x, y + 1), (x + 1, y)),
    Set((x, MaxY - 1), (x + 1, MaxY), (x + 1, MaxY - 1)),
    Set((MaxX - 1, y), (MaxX, y + 1)),
    Set((MaxX - 1, y), (MaxX - 1, y - 1), (MaxX, y - 1))
  )
