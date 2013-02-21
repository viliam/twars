package sk.kave.tank.beans

import sk.kave.tank._

object Game {

  val config = implicitly[Config]
  import config._

  def map = Map()
  def tank = new Tank((config.width - tankSize) /2, (config.height - tankSize) /2)
}
