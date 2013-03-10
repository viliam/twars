package sk.kave.tank.beans

import sk.kave.tank._
import fx.UP
import akka.actor.{TypedActor, TypedProps}

trait Game {

  trait IGameInit {
     def tankX : Int
     def tankY : Int
     def direction : Vector2D
  }

  val map = Map()

  val initG : IGameInit = new IGameInit {
    override val tankX = map.maxCols /2
    override val tankY = map.maxRows /2
    override val direction = (None, Some(UP))
  }

  val config :Config = ConfigImpl
  import config._

  lazy val tank : Tank = TypedActor(Main.system).typedActorOf(TypedProps(classOf[Tank],
                            new TankImpl(initG.tankX,initG.tankY)), "tank")


}
