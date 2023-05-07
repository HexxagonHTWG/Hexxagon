package lib.database.slick.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

import scala.annotation.targetName

class FieldTable(tag: Tag) extends Table[(Int, Int, Int, String)](tag, "field"):
  def gameId = column[Int]("game_id", O.PrimaryKey)

  def row = column[Int]("row", O.PrimaryKey)

  def col = column[Int]("column", O.PrimaryKey)

  def value = column[String]("value")

  def * = (gameId, row, col, value)

  def gameFk = foreignKey("game_fk", gameId, new TableQuery(new GameTable(_)))(targetColumns = _.id)
