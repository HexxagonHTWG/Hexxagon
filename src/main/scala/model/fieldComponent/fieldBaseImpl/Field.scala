package model.fieldComponent.fieldBaseImpl

import model.Player
import model.fieldComponent.{FieldInterface, MatrixInterface}

case class Field()(using val matrix: MatrixInterface[Player]) extends FieldInterface[Player]:

  private val EOL = "\n"

  override def fillAll(c: Player): FieldInterface[Player] = copy()(using matrix.fillAll(c))

  override def place(c: Player, x: Int, y: Int): FieldInterface[Player] = copy()(using matrix.fill(c, x, y))

  override def placeAlways(c: Player, x: Int, y: Int): FieldInterface[Player] = copy()(using matrix.fillAlways(c, x, y))

  override def reset: FieldInterface[Player] = copy()(using matrix.fillAll(Player.Empty))

  override def field: String =
    var res = EOL + edgeTop
    for (l <- 0 until matrix.row) res += (top(l) + bot(l))
    res += edgeBot
    res

  def edgeTop: String = " ___ " + "    ___ " * (matrix.col / 2) + EOL

  def edgeBot: String = " " + "   \\___/" * (matrix.col / 2) + EOL

  def bot(line: Int): String =
    var res = "\\___/"

    matrix.matrix(line).zipWithIndex.foreach(
      (x, i) => if i % 2 != 0 && i >= 1 && i < matrix.col then res += " " + x.toString + " \\___/")

    res + "\n"

  def top(line: Int): String =
    var res = "/ " + matrix.matrix(line)(0).toString + " \\"

    matrix.matrix(line).zipWithIndex.foreach(
      (x, i) => if i % 2 == 0 && i >= 2 then res += "___/ " + x + " \\")

    res + "\n"
