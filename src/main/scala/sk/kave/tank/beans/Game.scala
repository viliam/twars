package sk.kave.tank.beans

import sk.kave.tank._

object Game {

  val config = implicitly[Config]
  import config._

  val map = Map()
  lazy val tank = new Tank(map.maxCols /2, map.maxRows /2)
}
