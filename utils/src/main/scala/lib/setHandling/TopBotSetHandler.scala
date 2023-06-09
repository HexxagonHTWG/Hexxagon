package lib
package setHandling

import lib.Opposite

import scala.util.{Failure, Success, Try}

class TopBotSetHandler[T <: Opposite](content: T, x: Int, y: Int, startmatrix: Vector[Vector[T]]) extends DefaultSetHandler(content, x, y, startmatrix) {
  final protected val MaxY = startmatrix.size - 1

  override def coordinates: List[Set[(Int, Int)]] = List(
    Set((x, y + 1), (x - 1, 0), (x - 1, iBound), (x + 1, 0), (x + 1, iBound)),
    Set((x, y - 1), (x - 1, MaxY), (x - 1, iBound), (x + 1, MaxY), (x + 1, iBound))
  )

  override protected def nextHandler: Vector[Vector[T]] = new SideSetHandler(content, x, y, startmatrix).handle()

}
