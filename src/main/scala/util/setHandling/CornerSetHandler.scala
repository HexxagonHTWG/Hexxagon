package util
package setHandling

import scala.util.{Failure, Success, Try}

case class CornerSetHandler() extends SideSetHandler:
  override def createSetAndHandle(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]): Seq[Vector[Char]] =
    setBound(x, y)
    val tmpMatrix = startmatrix
    setMaxY(startmatrix)
    setMaxX(startmatrix)
    toLookAt = List(
      Set((x, y + 1), (x + 1, y)),
      Set((x, MaxY - 1), (x + 1, MaxY), (x + 1, MaxY - 1)),
      Set((MaxX - 1, y), (MaxX, y + 1)),
      Set((MaxX - 1, y), (MaxX - 1, y - 1), (MaxX, y - 1))
    )

    val map = toLookAt.map(x => Try(setForEach(x, tmpMatrix, content))).collect { case Success(x) => x }
    if map.isEmpty then startmatrix
    else map.head
