package util
package setHandling

import scala.util.{Failure, Success, Try}

class SideSetHandler[T <: Opposite](content: T, x: Int, y: Int, startmatrix: Vector[Vector[T]]) extends TopBotSetHandler(content, x, y, startmatrix):
  final protected val MaxX = startmatrix.head.size - 1

  override def coordinates: List[Set[(Int, Int)]] = List(
    Set((x, y - 1), (x, y + 1), (x + 1, iBound), (x + 1, iBound + 1)),
    Set((x, y - 1), (x, y + 1), (MaxX - 1, y - 1), (MaxX - 1, y))
  )

  override protected def nextHandler: Vector[Vector[T]] = CornerSetHandler(content, x, y, startmatrix).handle()

