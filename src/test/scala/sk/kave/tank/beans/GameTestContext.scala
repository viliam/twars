package sk.kave.tank.beans

import akka.actor.{TypedProps, Props}
import javafx.util.Duration
import scala.Predef._
import scala.Some
import sk.kave.tank.fx.UP
import sk.kave.tank.Config

/**
 * User: wilo
 */
class GameTestContext extends IGameContext {

  override lazy val tankActor = null //Main.system.actorOf(Props[TankActor])
  override lazy val map : Map = Map()
  override lazy val tank : Tank = null

  //override val (mapWidth, mapHeight) : (Int, Int) = Map.bound

  override val initG : IGameInit = new IGameInit {
    override val tankX = Map.mapWidth /2
    override val tankY = Map.mapHeight /2
    override val direction = (None, Some(UP))
  }

  override val config :Config = new Config() {
    def width = 10

    def height = 10

    def itemSize = 1

    def tankSize = 2

    def tankRotationDuration:Duration = 10 ms

    def tankMovementDuration:Duration = 10 ms

    def bulletMovementDuration : Duration = 10 ms
  }

}
