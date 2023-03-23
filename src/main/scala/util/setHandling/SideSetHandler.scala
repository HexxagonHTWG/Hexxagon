package util
package setHandling

import scala.util.{Failure, Success, Try}

class SideSetHandler(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]) extends TopBotSetHandler(content, x, y, startmatrix):
  final protected val MaxX = startmatrix.head.size - 1

  override def handle(): Seq[Vector[Char]] =
    val tmpMatrix = startmatrix

    val map = toLookAt.map(x => Try(setForEach(x, tmpMatrix, content))).collect { case Success(x) => x }
    if map.isEmpty then CornerSetHandler(content, x, y, startmatrix).handle()
    else map.head

  override def toLookAt: List[Set[(Int, Int)]] = List(
    Set((x, y - 1), (x, y + 1), (x + 1, iBound), (x + 1, iBound + 1)),
    Set((x, y - 1), (x, y + 1), (MaxX - 1, y - 1), (MaxX - 1, y))
  )

