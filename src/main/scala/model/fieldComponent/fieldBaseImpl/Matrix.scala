package model.fieldComponent.fieldBaseImpl

import model.fieldComponent.MatrixInterface
import util.SetHandling.DefaultSetHandler

case class Matrix(matrix: Vector[Vector[Char]], xCount: Int = 0, oCount: Int = 0) extends MatrixInterface[Char]:
  val col: Int = matrix(0).size
  val row: Int = matrix.size

  def this(col: Int, row: Int) =
    this(
      if (col < 0 || row < 0 || col > 10 || row > 10) // 10 not working with regex
        Vector.fill[Char](6, 9)(' ') // default values
      else if col % 2 == 0 then
        Vector.fill[Char](row, col + 1)(' ')
      else
        Vector.fill[Char](row, col)(' '),
      0, 0)

  override def cell(col: Int, row: Int): Char = matrix(row)(col)

  override def fillAll(content: Char): Matrix =
    var o, x = 0
    content match {
      case 'X' => x = MAX; o = 0
      case 'O' => o = MAX; x = 0
      case _ => o = 0; x = 0
    }
    copy(Vector.fill[Char](row, col)(content), x, o)

  def MAX: Int = row * col

  override def fill(content: Char, x: Int, y: Int): Matrix =
    if content.equals(' ') then copy(matrix.updated(y, matrix(y).updated(x, content)))

    var tmpMatrix = new DefaultSetHandler().createSetAndHandle(content, x, y, matrix)
    tmpMatrix = tmpMatrix.updated(y, tmpMatrix(y).updated(x, content))
    val xCount = tmpMatrix.flatten.count(x => x == 'X')
    val oCount = tmpMatrix.flatten.count(x => x == 'O')

    copy(tmpMatrix, xCount, oCount)

  override def fillAlways(content: Char, x: Int, y: Int): Matrix =
    val tmpMatrix = matrix.updated(y, matrix(y).updated(x, content))
    val xCount = tmpMatrix.flatten.count(x => x == 'X')
    val oCount = tmpMatrix.flatten.count(x => x == 'O')

    copy(tmpMatrix, xCount, oCount)
