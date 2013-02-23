package sk.kave.tank.actors

import sk.kave.tank._
import beans.Game
import fx._
import scalafx.Includes._
import akka.actor.{Props, ActorDSL, Actor}

/**
 * actor performing movement of tank.
 *
 * @author Vil
 */
class RotationActor extends Actor{

  val mapActor = context.actorOf( Props[MovementActor])
  val tank = Game.tank

  private var newVect : Vector2D = tank.vect
  private var isTimelineAlive =false

  def receive = {
    case (horizontal: Option[Horizontal], vertical: Option[Vertical])  =>
      if (!isTimelineAlive) {
        isTimelineAlive = true

        newVect = (horizontal, vertical)
        if (newVect != tank.vect ) {
          tank() = newVect
        }

        //mapActor ! newVect
      }
//        } else {
//          Main.controlerActor ! Action.CONTINUE
//        }
    case Action.CONTINUE =>        //when one key si released, actor needs to continue
      isTimelineAlive = false

    case m @ AnyRef => logg.debug("RotationActor : Unknow message = " + m)
  }


}
