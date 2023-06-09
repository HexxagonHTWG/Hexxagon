package lib
package setHandling

import lib.Opposite

import scala.util.{Failure, Success, Try}

class DefaultSetHandler[T <: Opposite](content: T, x: Int, y: Int, startmatrix: Vector[Vector[T]]) extends SetHandler(content, x, y, startmatrix):

  final protected val iBound: Int =
    y - 1 + (if x % 2 == 1 || y - 1 < 0 then 1 + (if x % 2 == 1 && y == 0 then 1 else 0) else 0)

  override def coordinates: List[Set[(Int, Int)]] =
    List(
      Set((x, y - 1), (x, y + 1), (x - 1, iBound), (x - 1, iBound + 1), (x + 1, iBound), (x + 1, iBound + 1))
    )

  override protected def nextHandler: Vector[Vector[T]] = new TopBotSetHandler(content, x, y, startmatrix).handle()
