package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._
import events.EventTrait
import events.mapchanged.MapChangedEvent
import fx._
import sk.kave.tank.map
import scala.Some
import scala.Some


private[beans] object Map {

  lazy val m: Map = readMapFromFile("mapa.mapa")

  def apply() = m
}

class Map(val items: COLUMNS) extends EventTrait[MapChangedEvent] {

  val maxCols: Int = items.size
  val maxRows: Int = items(0).size

  def apply(c: Int, r: Int): Items = {
    if (r >= maxRows || r < 0 || c >= maxCols || c < 0) {
      return NoMap
    }
    items(c)(r)
  }

  def update(c: Int, r: Int,  newValue: Items) {
    logg.debug("update map  col: "+ c + "row: " + r + "  item: " + newValue)

    items(c)(r) = newValue

    fireEvent(new MapChangedEvent( c, r, newValue))
  }


  /*
  For give position and bounds return if is posible move to specific direction
   */
  def canMove(position : => (Int, Int),
              bounds   : => (Int, Int),
              direction: => Vector2D): Boolean = {
      val (col,row) = position
      val (width,height) = bounds
      val (vertical, horizontal) = direction
      val result =
        (vertical match {
          case Some(LEFT)  if (col <= 0) =>   false
          case Some(RIGHT) if (col >= maxCols - width) =>  false
          case _ => true
        }) &&
          (horizontal match {
            case Some(UP)   if (row <= 0) => false
            case Some(DOWN) if (row >= maxRows - height) => false
            case _ => true
          })
      if (!result) {
        logg.debug("tank, cannot move")
      }

      result
    }

}
