package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._
import events.EventTrait
import events.mapchanged.MapChangedEvent


private[beans] object Map {

  lazy val m: Map = readMapFromFile("mapa.mapa")

  def apply() = m
}

class Map(val items: ROWS) extends EventTrait[MapChangedEvent] {

  val maxRows: Int = items.size
  val maxCols: Int = items(0).size

  def apply(c: Int, r: Int): Items = {
    if (r >= maxRows || r < 0 || c >= maxCols || c < 0) {
      return NoMap
    }
    items(r)(c)
  }

  def update(r: Int, c: Int, newValue: Items) {
    items(r)(c) = newValue

    fireEvent(new MapChangedEvent(r, c, newValue))
  }


}
