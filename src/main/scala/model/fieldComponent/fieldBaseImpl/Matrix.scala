package model.fieldComponent.fieldBaseImpl

import model.Player
import model.fieldComponent.MatrixInterface
import util.setHandling.DefaultSetHandler

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
    val (x, o) = content match {
      case Player.X => (MAX, 0)
      case Player.O => (0, MAX)
      case _ => (0, 0)
    }
    copy(Vector.fill[Player](row, col)(content), x, o)

  def MAX: Int = row * col

  override def fill(content: Player, x: Int, y: Int): Matrix =
    if content.equals(Player.Empty) then copy(matrix)

    val tmpMatrix = new DefaultSetHandler(content, x, y, matrix).handle()
    fillAlways(content, x, y, tmpMatrix)

  override def fillAlways(content: Player, x: Int, y: Int, matrix: Vector[Vector[Player]] = matrix): Matrix =
    val m = matrix.updated(y, matrix(y).updated(x, content))
    val (xCount, oCount) = (m.flatten.count(_.equals(Player.X)), m.flatten.count(_.equals(Player.O)))
    copy(m, xCount, oCount)
