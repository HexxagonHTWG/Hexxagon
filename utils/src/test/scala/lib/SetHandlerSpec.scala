package lib

import lib.Opposite
import lib.setHandling.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

enum Player extends Opposite:
  case X extends Player
  case O extends Player

  override def other: Player = this match
    case X => O
    case O => X

class SetHandlerSpec extends AnyWordSpec:
  "Looking at indices,a matrix and a SetHandler" when {
    "created and used by a client" should {
      var mat = Vector.fill(5)(Vector.fill(5)(Player.O))
      "give back the same matrix when nothing fits" in {
        mat = Vector.fill(5)(Vector.fill(5)(Player.O))
        new DefaultSetHandler(Player.X, -1, 5, mat).handle() should be(mat)
        new DefaultSetHandler(Player.X, 6, 5, mat).handle() should be(CornerSetHandler(Player.X, 6, 6, mat).handle())
      }
      "handle each case with a different class" in {
        mat = Vector.fill(5)(Vector.fill(5)(Player.O))
        new DefaultSetHandler(Player.X, 2, 0, mat).handle() should be(new TopBotSetHandler(Player.X, 2, 0, mat).handle())
        new DefaultSetHandler(Player.X, 0, 2, mat).handle() should be(new SideSetHandler(Player.X, 0, 2, mat).handle())
        new DefaultSetHandler(Player.X, 0, 0, mat).handle() should be(CornerSetHandler(Player.X, 0, 0, mat).handle())
        CornerSetHandler(Player.X, 0, 0, mat).handle() should be(new DefaultSetHandler(Player.X, 0, 0, mat).handle())
        CornerSetHandler(Player.X, 1, 1, mat).handle() should not be new DefaultSetHandler(Player.X, 1, 1, mat).handle()
      }
      "have a certain List of Sets for each Situation" in {
        new DefaultSetHandler(Player.X, 3, 3, mat).coordinates should contain allElementsOf List(Set((3, 2), (3, 4), (2, 3), (2, 4), (4, 3), (4, 4)))
        new TopBotSetHandler(Player.X, 3, 0, mat).coordinates should contain allElementsOf List(Set((2, 1), (2, 0), (3, 1), (4, 0), (4, 1)),
          Set((3, -1), (2, 4), (4, 1), (4, 4), (2, 1))) // Bot even tho we are at the top
        new SideSetHandler(Player.X, 0, 3, mat).coordinates should contain allElementsOf List(Set((0, 2), (0, 4), (1, 2), (1, 3)),
          Set((0, 2), (0, 4), (3, 2), (3, 3)))
        CornerSetHandler(Player.X, 0, 0, mat).coordinates should contain allElementsOf List(Set((0, 1), (1, 0)),
          Set((3, 0), (4, 1)),
          Set((0, 3), (1, 4), (1, 3)),
          Set((3, -1), (3, 0), (4, -1)))
      }
      "is a chain of responsibility" in {
        val (c, x, y, m) = (Player.X, 0, 0, mat)
        CornerSetHandler(c, x, y, m).isInstanceOf[SideSetHandler[Player]] should be(true)
        CornerSetHandler(c, x, y, m).isInstanceOf[TopBotSetHandler[Player]] should be(true)
        CornerSetHandler(c, x, y, m).isInstanceOf[DefaultSetHandler[Player]] should be(true)
        SideSetHandler(c, x, y, m).isInstanceOf[TopBotSetHandler[Player]] should be(true)
        SideSetHandler(c, x, y, m).isInstanceOf[DefaultSetHandler[Player]] should be(true)
        TopBotSetHandler(c, x, y, m).isInstanceOf[DefaultSetHandler[Player]] should be(true)
      }
    }
  }
  "A SetHandler interface" should {
    var mat = Vector.fill(5)(Vector.fill(5)(Player.O))
    "have a handle method" in {
      val (c, x, y, m) = (Player.X, 0, 0, mat)
      val handler = new DefaultSetHandler(c, x, y, m).asInstanceOf[SetHandler[Player]]
      handler.handle() should be(CornerSetHandler(c, x, y, m).asInstanceOf[SetHandler[Player]].handle())
    }
    "be generic" when {
      "used with a Player" in {
        val (c, x, y, m) = (Player.X, 0, 0, mat)
        val handler = new DefaultSetHandler(c, x, y, m).asInstanceOf[SetHandler[Player]]
        handler.handle() should be(CornerSetHandler(c, x, y, m).asInstanceOf[SetHandler[Player]].handle())
      }
      "used with a anything extending the Opposite trait" in {
        val (c, x, y, m) = (new MockOpposite("A"), 0, 0, mat)
        val handler = new DefaultSetHandler(c, x, y, m).asInstanceOf[SetHandler[Opposite]]
        handler.handle() should be(CornerSetHandler(c, x, y, m).asInstanceOf[SetHandler[Opposite]].handle())
      }
    }
  }
