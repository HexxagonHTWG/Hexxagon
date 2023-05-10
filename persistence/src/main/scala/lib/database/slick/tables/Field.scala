package lib.database.slick.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import scala.annotation.targetName

class Field(tag: Tag) extends Table[(Int, Int, Int, String)](tag, "field"):

  def * = (gameId, row, col, value)

  def gameId = column[Int]("game_id")

  def row = column[Int]("row")

  def col = column[Int]("column")

  def value = column[String]("value")

  def pk = primaryKey("pk_field", (gameId, row, col))
