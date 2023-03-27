package util
package setHandling

import model.Player
import scala.util.{Failure, Success, Try}

class SideSetHandler(content: Player, x: Int, y: Int, startmatrix: Vector[Vector[Player]]) extends TopBotSetHandler(content, x, y, startmatrix):
  final protected val MaxX = startmatrix.head.size - 1

  override def coordinates: List[Set[(Int, Int)]] = List(
    Set((x, y - 1), (x, y + 1), (x + 1, iBound), (x + 1, iBound + 1)),
    Set((x, y - 1), (x, y + 1), (MaxX - 1, y - 1), (MaxX - 1, y))
  )

  override protected def nextHandler: Vector[Vector[Player]] = CornerSetHandler(content, x, y, startmatrix).handle()

