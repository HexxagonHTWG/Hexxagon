package util
package setHandling

import scala.util.{Failure, Success, Try}

case class CornerSetHandler(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]) extends SideSetHandler(content, x, y, startmatrix):

  override def handle(): Seq[Vector[Char]] =
    val tmpMatrix = startmatrix

    val map = toLookAt.map(x => Try(setForEach(x, tmpMatrix, content))).collect { case Success(x) => x }
    if map.isEmpty then startmatrix
    else map.head

  override def toLookAt: List[Set[(Int, Int)]] = List(
    Set((x, y + 1), (x + 1, y)),
    Set((x, MaxY - 1), (x + 1, MaxY), (x + 1, MaxY - 1)),
    Set((MaxX - 1, y), (MaxX, y + 1)),
    Set((MaxX - 1, y), (MaxX - 1, y - 1), (MaxX, y - 1))
  )
