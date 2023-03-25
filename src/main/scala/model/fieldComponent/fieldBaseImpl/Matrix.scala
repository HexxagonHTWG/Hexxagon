package model.fieldComponent.fieldBaseImpl

import model.fieldComponent.MatrixInterface
import util.setHandling.DefaultSetHandler
import model.Player

case class Matrix(matrix: Vector[Vector[Player]], xCount: Int = 0, oCount: Int = 0) extends MatrixInterface[Player]:
  val col: Int = matrix(0).size
  val row: Int = matrix.size

  def this(col: Int, row: Int) =
    this(
      if (col < 0 || row < 0 || col > 10 || row > 10) // 10 not working with regex
        Vector.fill[Player](6, 9)(Player.Empty) // default values
      else if col % 2 == 0 then
        Vector.fill[Player](row, col + 1)(Player.Empty)
      else
        Vector.fill[Player](row, col)(Player.Empty),
      0, 0)

  override def cell(col: Int, row: Int): Player = matrix(row)(col)

  override def fillAll(content: Player): Matrix =
    var o, x = 0
    content match {
      case Player.X => x = MAX; o = 0
      case Player.O => o = MAX; x = 0
      case _ => o = 0; x = 0
    }
    copy(Vector.fill[Player](row, col)(content), x, o)

  def MAX: Int = row * col

  override def fill(content: Player, x: Int, y: Int): Matrix =
    if content.equals(Player.Empty) then copy(matrix.updated(y, matrix(y).updated(x, content)))

    var tmpMatrix = new DefaultSetHandler(content, x, y, matrix).handle()
    tmpMatrix = tmpMatrix.updated(y, tmpMatrix(y).updated(x, content))
    val xCount = tmpMatrix.flatten.count(x => x == Player.X)
    val oCount = tmpMatrix.flatten.count(x => x == Player.O)

    copy(Vector.from(tmpMatrix), xCount, oCount)

  override def fillAlways(content: Player, x: Int, y: Int): Matrix =
    val tmpMatrix = matrix.updated(y, matrix(y).updated(x, content))
    val xCount = tmpMatrix.flatten.count(x => x == Player.X)
    val oCount = tmpMatrix.flatten.count(x => x == Player.O)

    copy(tmpMatrix, xCount, oCount)
