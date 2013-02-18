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
   require(r < items.size || r >= 0 || c < items(r).size || c >= 0)
    items(r)(c)
  }

  val maxRows:Int = items.size
  val maxCols:Int = items(0).size
}
