package util
package setHandling

import scala.util.{Failure, Success, Try}

class TopBotSetHandler(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]) extends DefaultSetHandler(content, x, y, startmatrix) {
  final protected val MaxY = startmatrix.size - 1

  override def coordinates: List[Set[(Int, Int)]] = List(
    Set((x, y + 1), (x - 1, 0), (x - 1, iBound), (x + 1, 0), (x + 1, iBound)),
    Set((x, y - 1), (x - 1, MaxY), (x - 1, iBound), (x + 1, MaxY), (x + 1, iBound))
  )

  override protected def nextHandler: Seq[Vector[Char]] = new SideSetHandler(content, x, y, startmatrix).handle()

}
