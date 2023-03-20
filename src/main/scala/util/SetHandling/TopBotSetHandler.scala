package util
package SetHandling

import scala.util.{Failure, Success, Try}

class TopBotSetHandler() extends DefaultSetHandler {
  var MaxY = 0

  override def createSetAndHandle(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]): Vector[Vector[Char]] =
    setBound(x, y)
    val tmpMatrix = startmatrix
    setMaxY(startmatrix)
    toLookAt = List(Set((x, y + 1), (x - 1, 0), (x - 1, iBound), (x + 1, 0), (x + 1, iBound)),
      Set((x, y - 1), (x - 1, MaxY), (x - 1, iBound), (x + 1, MaxY), (x + 1, iBound)))

    val map = toLookAt.map(x => Try(setForEach(x, tmpMatrix, content))).collect { case Success(x) => x }
    if map.isEmpty then new SideSetHandler().createSetAndHandle(content, x, y, startmatrix)
    else map.head

  def setMaxY(ma: Vector[Vector[Char]]): Unit = MaxY = ma.size - 1
}
