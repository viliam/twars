package sk.kave.tank.beans

import sk.kave.tank._

trait Game {

  val config :Config = ConfigImpl
  import config._

  val map = Map()
  lazy val tank = new Tank(map.maxCols /2, map.maxRows /2)
}
