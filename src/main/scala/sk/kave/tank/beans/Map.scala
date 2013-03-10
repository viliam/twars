package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._
import events.{MapChangeEvent, EventListener}
import fx._
import scala.Some
import utils.Logger


private[beans] object Map extends Logger {

  lazy val m: Map = readMapFromFile("mapaGround.mapa")

  def apply() = m
}

class Map(val items: COLUMNS) extends EventListener[MapChangeEvent] {

  val maxCols: Int = items.size
  val maxRows: Int = items(0).size

  def apply(c: Int, r: Int): Items = {
    if (r >= maxRows || r < 0 || c >= maxCols || c < 0) {
      return NoMap
    }
    items(c)(r)
  }

  def update(c: Int, r: Int,  newValue: Items) {
//    debug("update map  col: "+ c + "row: " + r + "  item: " + newValue, Vilo)

    items(c)(r) = newValue

    fireEvent( MapChangeEvent( c, r, newValue))
  }


  /*
  For given position and bounds return if is posible move to specific direction
   */
  def canMove(position : => (Int, Int),
              bounds   : => (Int, Int),
              direction: => Vector2D): Boolean = {
      val (col,row) = position
      val (width,height) = bounds
      val (horizontal, vertical) = direction
      val result =
        (horizontal match {
          case Some(LEFT)  if (col <= 0) =>   false
          case Some(RIGHT) if (col >= maxCols - width) =>  false
          case _ => true
        })&&
          (vertical match {
            case Some(UP)   if (row <= 0) => false
            case Some(DOWN) if (row >= maxRows - height) => false
            case _ => true
          })
      if (!result) {
        debug("cannot move",All)
      }

      result
    }

}
