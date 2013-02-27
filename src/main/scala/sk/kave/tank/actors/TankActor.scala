package sk.kave.tank.actors

import sk.kave.tank._
import beans.Game
import akka.actor.Actor
import utils.Logger

/**
 * actor performing movement of tank.
 *
 * @author Vil
 */
class TankActor extends Actor with Logger {

  val tank = Game.tank

  private var newVect : Vector2D = tank.vect
  private var lock =false

  def receive = {
    case NewDirection( newDirection : Vector2D)  =>
      if (!lock) {
        logg.debug("TankActor:  lock")
        lock = true

        newVect = newDirection
        if (newVect != tank.vect ) {
          tank.changeDirection( newVect ) {
            () => self ! UnLock
          }
        } else {
          tank.move( newVect) {
            () => self ! UnLock
          }
        }

      } else {
        logg.debug("RotationActor: message is ignoring " + newDirection)
      }

    case UnLock =>        //when one key si released, actor needs to continue
      lock = false
      logg.debug("RotationActor: unlock actor" + newVect)

    case m @ AnyRef => logg.warn("RotationActor : Unknow message = " + m)
  }



}
