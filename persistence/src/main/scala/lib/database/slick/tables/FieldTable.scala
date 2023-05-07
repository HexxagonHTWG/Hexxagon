package lib.database.slick.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import scala.annotation.targetName

class FieldTable(tag: Tag) extends Table[(Int, Int, Int, Int, String)](tag, "field"):
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def gameId = column[Int]("game_id")

  def row = column[Int]("row")

  def col = column[Int]("column")

  def value = column[String]("value")

  def * = (id, gameId, row, col, value)
