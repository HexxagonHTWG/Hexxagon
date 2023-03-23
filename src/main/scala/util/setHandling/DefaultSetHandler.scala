package util
package setHandling

import scala.util.{Failure, Success, Try}

class DefaultSetHandler(content: Char, x: Int, y: Int, startmatrix: Vector[Vector[Char]]) extends SetHandler:

  override def handle(): Seq[Vector[Char]] =
    if x < 0 || y < 0 then return startmatrix
    val tmpMatrix = startmatrix

    Try(setForEach(toLookAt.head, tmpMatrix, content)) match {
      case Success(o) => o
      case Failure(_) => new TopBotSetHandler(content, x, y, startmatrix).handle()
    }

  override def toLookAt: List[Set[(Int, Int)]] =
    List(
      Set((x, y - 1), (x, y + 1), (x - 1, iBound), (x - 1, iBound + 1), (x + 1, iBound), (x + 1, iBound + 1))
    )

  final protected def iBound: Int =
    var t = y - 1
    if x % 2 == 1 || t < 0 then
      t += 1
      if x % 2 == 1 && t == 0 then
        t += 1
    t
