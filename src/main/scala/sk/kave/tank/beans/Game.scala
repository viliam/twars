package sk.kave.tank.beans

import sk.kave.tank._
import actors.TankActor
import fx.UP
import akka.actor.{Props, TypedActor, TypedProps}

trait Game {

  trait IGameInit {
     def tankX : Int
     def tankY : Int
     def direction : Vector2D
  }

  lazy val tankActor = Main.system.actorOf(Props[TankActor])
  lazy val map : Map = TypedActor( Main.system).typedActorOf( TypedProps( classOf[Map], Map()), "map")
  val (mapWidth, mapHeight) : (Int, Int) = Map.bound

  val initG : IGameInit = new IGameInit {
    override val tankX = mapWidth /2
    override val tankY = mapHeight /2
    override val direction = (None, Some(UP))
  }

  val config :Config = ConfigImpl
  import config._

  lazy val tank : Tank = TypedActor(Main.system).typedActorOf(TypedProps(classOf[Tank],
                            new TankImpl(initG.tankX,initG.tankY)), "tank")


}
