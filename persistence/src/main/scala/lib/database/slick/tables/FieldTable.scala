package lib.database.slick.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import scala.annotation.targetName

class FieldTable(tag: Tag) extends Table[(Int, Int, Int, String)](tag, "field"):
  def gameId = column[Int]("game_id")

  def row = column[Int]("row")

  def col = column[Int]("column")

  def value = column[String]("value")

  def * = (gameId, row, col, value)

  def pk = primaryKey("pk_notifications", (gameId, row, col))

  def gameFk = foreignKey("game_fk", gameId, new TableQuery(new GameTable(_)))(targetColumns = _.id)
