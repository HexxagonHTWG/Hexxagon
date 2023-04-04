package util

import org.scalatest.matchers.should.Matchers.shouldEqual
import org.scalatest.wordspec.AnyWordSpec

class OppositeSpec extends AnyWordSpec:
  "Opposite" should {
    "return another Opposite" in {
      new Opposite:
        override def other: Opposite = new Opposite:
          override def other: Opposite = this
    }
    "return another Opposite with a different input" in {
      val opposite = new MockOpposite("A")
      opposite.other.getInput shouldEqual "AB"
    }
  }

class MockOpposite(input: String) extends Opposite:
  override def other: MockOpposite = new MockOpposite(input + "B")

  def getInput: String = input
