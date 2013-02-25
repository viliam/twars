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
  @volatile private var isTimelineAlive =false

  def receive = {
    case NewDirection( newDirection : Vector2D)  =>
      if (!isTimelineAlive) {
        newVect = newDirection
        if (newVect != tank.vect ) {
          logg.debug("RotationActor :  lock")
          isTimelineAlive = true

          tank() = newVect
        }

      } else {
        logg.debug("RotationActor: message is ignoring")
      }

    case UnLock =>        //when one key si released, actor needs to continue
      isTimelineAlive = false
      logg.debug("RotationActor: unlock actor")
    case m @ AnyRef => logg.debug("RotationActor : Unknow message = " + m)
  }


}
