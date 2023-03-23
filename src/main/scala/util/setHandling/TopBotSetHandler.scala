package util
package setHandling

import scala.util.{Failure, Success, Try}

class TopBotSetHandler(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]) extends DefaultSetHandler(content, x, y, startmatrix) {
  final protected val MaxY = startmatrix.size - 1

  override def handle(): Seq[Vector[Char]] =
    val tmpMatrix = startmatrix

    val map = toLookAt.map(x => Try(setForEach(x, tmpMatrix, content))).collect { case Success(x) => x }
    if map.isEmpty then new SideSetHandler(content, x, y, startmatrix).handle()
    else map.head

  override def toLookAt: List[Set[(Int, Int)]] = List(
    Set((x, y + 1), (x - 1, 0), (x - 1, iBound), (x + 1, 0), (x + 1, iBound)),
    Set((x, y - 1), (x - 1, MaxY), (x - 1, iBound), (x + 1, MaxY), (x + 1, iBound))
  )

}
