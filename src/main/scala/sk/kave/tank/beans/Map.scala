package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._


object Map {

  lazy val m: Map = readMapFromFile("mapa.mapa")

  def apply() = m
}

class Map(val items: ROWS) {

  def apply(r: Int, c: Int): Items = {
    if (r >= maxRows || r < 0 || c >= maxCols || c < 0) {
      return NoMap
    }
    items(r)(c)
  }

  val maxRows: Int = items.size
  val maxCols: Int = items(0).size
}
