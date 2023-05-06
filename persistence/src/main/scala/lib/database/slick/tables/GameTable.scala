package lib.database.slick.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

class GameTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "game"):
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  
  def rows = column[Int]("rows")

  def columns = column[Int]("columns")
  
  def * = (id, rows, columns)
