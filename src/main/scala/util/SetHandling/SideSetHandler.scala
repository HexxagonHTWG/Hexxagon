package util
package SetHandling

import scala.util.{Failure, Success, Try}

class SideSetHandler() extends TopBotSetHandler:
  var MaxX = 0

  override def createSetAndHandle(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]): Seq[Vector[Char]] =
    setBound(x, y)
    val tmpMatrix = startmatrix
    setMaxY(startmatrix)
    setMaxX(startmatrix)
    toLookAt = List(Set((x, y - 1), (x, y + 1), (x + 1, iBound), (x + 1, iBound + 1)),
      Set((x, y - 1), (x, y + 1), (MaxX - 1, y - 1), (MaxX - 1, y)))

    val map = toLookAt.map(x => Try(setForEach(x, tmpMatrix, content))).collect { case Success(x) => x }
    if map.isEmpty then CornerSetHandler().createSetAndHandle(content, x, y, startmatrix)
    else map.head

  def setMaxX(ma: Vector[Vector[Char]]): Unit = MaxX = ma(0).size - 1
