package util

import model.Player
import model.fieldComponent.fieldBaseImpl.Matrix
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import util.setHandling.*

class SetHandlerSpec extends AnyWordSpec:
  "Looking at indices and a matrix a SetHandler" when {
    "created and used by a client" should {
      var mat = new Matrix(5, 5)
      "give back the same matrix when nothing fits" in {
        mat = mat.fillAll(Player.O)
        new DefaultSetHandler(Player.X, -1, 5, mat.matrix).handle() should be(mat.matrix)
        new DefaultSetHandler(Player.X, 6, 5, mat.matrix).handle() should be(CornerSetHandler(Player.X, 6, 6, mat.matrix).handle())
      }
      "handle each case with a different class" in {
        mat = mat.fillAll(Player.O)
        new DefaultSetHandler(Player.X, 2, 0, mat.matrix).handle() should be(new TopBotSetHandler(Player.X, 2, 0, mat.matrix).handle())
        new DefaultSetHandler(Player.X, 0, 2, mat.matrix).handle() should be(new SideSetHandler(Player.X, 0, 2, mat.matrix).handle())
        new DefaultSetHandler(Player.X, 0, 0, mat.matrix).handle() should be(CornerSetHandler(Player.X, 0, 0, mat.matrix).handle())
        CornerSetHandler(Player.X, 0, 0, mat.matrix).handle() should be(new DefaultSetHandler(Player.X, 0, 0, mat.matrix).handle())
        CornerSetHandler(Player.X, 1, 1, mat.matrix).handle() should not be new DefaultSetHandler(Player.X, 1, 1, mat.matrix).handle()
      }
      "have a certain List of Sets for each Situation" in {
        new DefaultSetHandler(Player.X, 3, 3, mat.matrix).coordinates should contain allElementsOf List(Set((3, 2), (3, 4), (2, 3), (2, 4), (4, 3), (4, 4)))
        new TopBotSetHandler(Player.X, 3, 0, mat.matrix).coordinates should contain allElementsOf List(Set((2, 1), (2, 0), (3, 1), (4, 0), (4, 1)),
          Set((3, -1), (2, 4), (4, 1), (4, 4), (2, 1))) // Bot even tho we are at the top
        new SideSetHandler(Player.X, 0, 3, mat.matrix).coordinates should contain allElementsOf List(Set((0, 2), (0, 4), (1, 2), (1, 3)),
          Set((0, 2), (0, 4), (3, 2), (3, 3)))
        CornerSetHandler(Player.X, 0, 0, mat.matrix).coordinates should contain allElementsOf List(Set((0, 1), (1, 0)),
          Set((3, 0), (4, 1)),
          Set((0, 3), (1, 4), (1, 3)),
          Set((3, -1), (3, 0), (4, -1)))
      }
      "is a chain of responsibility" in {
        val (c, x, y, m) = (Player.X, 0, 0, mat.matrix)
        CornerSetHandler(c, x, y, m).isInstanceOf[SideSetHandler] should be(true)
        CornerSetHandler(c, x, y, m).isInstanceOf[TopBotSetHandler] should be(true)
        CornerSetHandler(c, x, y, m).isInstanceOf[DefaultSetHandler] should be(true)
        SideSetHandler(c, x, y, m).isInstanceOf[TopBotSetHandler] should be(true)
        SideSetHandler(c, x, y, m).isInstanceOf[DefaultSetHandler] should be(true)
        TopBotSetHandler(c, x, y, m).isInstanceOf[DefaultSetHandler] should be(true)
      }
    }
  }
