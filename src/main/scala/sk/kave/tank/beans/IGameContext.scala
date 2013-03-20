package sk.kave.tank.beans

import sk.kave.tank._
import akka.actor.ActorRef
import sk.kave.tank.{ConfigImpl, Config}

/**
 * User: vilo
 */
trait IGameContext {

  trait IGameInit {
    def tankX : Int
    def tankY : Int
    def direction : Vector2D
  }

  def tankActor : ActorRef
  def map : Map

//  val (mapWidth, mapHeight) : (Int, Int) = Map.bound  //not nice :((

  def initG : IGameInit

  val config : Config = ConfigImpl

  def tank : Tank
}
