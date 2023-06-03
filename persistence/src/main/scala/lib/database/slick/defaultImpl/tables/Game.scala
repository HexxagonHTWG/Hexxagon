package lib.database.slick.defaultImpl.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

class Game(tag: Tag) extends Table[(Int, Int, Int)](tag, "game"):
  def * = (id, rows, columns)

  def id = column[Int]("id", O.PrimaryKey)

  def rows = column[Int]("rows")

  def columns = column[Int]("columns")
