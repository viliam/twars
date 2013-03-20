package sk.kave.tank.beans

import sk.kave.tank._
import actors.TankActor
import fx.UP
import akka.actor.{Props, TypedActor, TypedProps}

trait GameContextImpl extends IGameContext {

  lazy val tankActor = Main.system.actorOf(Props[TankActor])
  lazy val map : Map = TypedActor( Main.system).typedActorOf( TypedProps( classOf[Map], Map()), "map")

  val initG : IGameInit = new IGameInit {
    override val tankX = Map.mapWidth /2
    override val tankY = Map.mapHeight /2
    override val direction = (None, Some(UP))
  }

  override val config :Config = ConfigImpl

  lazy val tank : Tank = TypedActor(Main.system).typedActorOf(TypedProps(classOf[Tank],
                            new TankImpl(initG.tankX,initG.tankY)), "tank")


}
