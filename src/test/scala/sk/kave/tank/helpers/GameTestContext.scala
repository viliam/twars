package sk.kave.tank.helpers

import scala.Predef._
import scala.Some
import sk.kave.tank.beans.Map
import sk.kave.tank.fx.UP
import sk.kave.tank.Config
import sk.kave.tank.beans.{TankImpl, Tank, IGameContext}

/**
 * User: wilo
 */
class GameTestContext extends IGameContext {

  override lazy val tankActor = null //Main.system.actorOf(Props[TankActor])
  override lazy val map : Map = Map("mapaGround.mapa")

  override val (mapWidth, mapHeight) : (Int, Int) = map.bound

  override val initG : IGameInit = new IGameInit {
    override val tankX = mapWidth /2
    override val tankY = mapHeight /2
    override val direction = (None, Some(UP))
  }

  override lazy val tank : Tank = new TankImpl(initG.tankX,initG.tankY)

  override val config :Config = new Config() {
    val width = 10
    val height = 10
    val itemSize = 1
    val tankSize = 2
  }

}
