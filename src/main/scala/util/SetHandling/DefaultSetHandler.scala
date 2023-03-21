package util
package SetHandling

import scala.util.{Failure, Success, Try}

class DefaultSetHandler() extends SetHandler:
  var iBound: Int = 0
  var toLookAt: List[Set[(Int, Int)]] = List()

  override def createSetAndHandle(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]): Seq[Vector[Char]] =
    if x < 0 || y < 0 then return startmatrix
    else
      setBound(x, y)
    val tmpMatrix = startmatrix
    toLookAt = List(Set((x, y - 1), (x, y + 1), (x - 1, iBound), (x - 1, iBound + 1), (x + 1, iBound), (x + 1, iBound + 1)))

    Try(setForEach(toLookAt.head, tmpMatrix, content)) match {
      case Success(o) => o
      case Failure(_) => new TopBotSetHandler().createSetAndHandle(content, x, y, startmatrix)
    }

  def setBound(x: Int, y: Int): Unit =
    iBound = y - 1
    if x % 2 == 1 || iBound < 0 then
      iBound += 1
      if x % 2 == 1 && iBound == 0 then
        iBound += 1
