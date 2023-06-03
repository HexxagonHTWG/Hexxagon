package lib.database.slick.jsonImpl.tables

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.TableQuery

class GameJson(tag: Tag) extends Table[(Int, String)](tag, "gameJson"):
  def * = (id, json)

  def id = column[Int]("id", O.PrimaryKey)

  def json = column[String]("json")
