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
    (Seq(EOL + edgeTop) ++ (0 until matrix.row).map { l => top(l) + bot(l) } :+ edgeBot).mkString

  def edgeTop: String = " ___ " + "    ___ " * (matrix.col / 2) + EOL

  def edgeBot: String = " " + "   \\___/" * (matrix.col / 2) + EOL

  def bot(line: Int): String =
    s"\\___/${
      matrix.matrix(line).zipWithIndex
        .filter { case (_, i) => i % 2 != 0 && i >= 1 && i < matrix.col }
        .map { case (x, _) => s" $x " }
        .mkString("\\___/")
    }\\___/$EOL"

  def top(line: Int): String =
    s"/ ${matrix.matrix(line)(0)} \\${
      matrix.matrix(line).zipWithIndex
        .filter { case (_, i) => i % 2 == 0 && i >= 2 }
        .map { case (x, _) => s" $x " }
        .mkString("___/", "\\___/", "\\")
    }$EOL"
