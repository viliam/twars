package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._


object Map {

  lazy val m: Map = readMapFromFile("mapa2.mapa")

  def apply() = m
}

class Map(val items: ROWS) {

  def apply(r: Int, c: Int): Items = {
    items(r)(c)
  }
}
