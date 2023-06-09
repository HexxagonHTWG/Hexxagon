package lib
package setHandling

import lib.Opposite

import scala.util.{Failure, Success, Try}

/**
 * Trait that gives the wireframe for handling placement of stones
 *
 * Child classes simply change coordinates and the next strategy/handler
 */
trait SetHandler[T <: Opposite](content: T, x: Int, y: Int, startmatrix: Vector[Vector[T]]):

  /**
   * Tries to change all stones in given coordinates,
   * if an error occurs, the next strategy/handler is called to do the same
   * else it returns the newly collected sequence
   *
   * @return the resulting matrix as sequence of vectors
   */
  final def handle(): Vector[Vector[T]] =
    if x < 0 || y < 0 then return startmatrix
    coordinates.map(x => Try(setForEach(x, startmatrix, content)))
      .collectFirst { case Success(x) => x } match
      case Some(h) => h
      case None => nextHandler

  /**
   * Helper function that sets a given set of coordinates inside a matrix to a given T
   *
   * @param s      set of coordinates to be changed
   * @param matrix matrix of which stones should be changed
   * @param player the replacement stone/character
   * @return the resulting matrix with changed coordinates
   */
  private final def setForEach(s: Set[(Int, Int)], matrix: Vector[Vector[T]], player: T): Vector[Vector[T]] = {
    var tmpMatrix = matrix
    s.foreach {
      (x, y) =>
        if tmpMatrix(y)(x).equals(player.other) then
          tmpMatrix = tmpMatrix.updated(y, tmpMatrix(y).updated(x, player))
    }
    tmpMatrix
  }

  /**
   * Next handler in line
   *
   * @return a fallback matrix, preferably a different implementation of SetHandler.handle()
   */
  protected def nextHandler: Vector[Vector[T]]

  /**
   * The current coordinates to be looked at in the handle() method
   *
   * @return a List of Coordinates
   */
  protected def coordinates: List[Set[(Int, Int)]]
