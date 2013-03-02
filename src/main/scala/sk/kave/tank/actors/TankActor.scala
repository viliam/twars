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

  private var direction: Vector2D = tank.vect
  private var lock = false

  def receive = {
    case NewDirection(newDirection: Vector2D) =>
      if (!lock) {
        debug("TankActor:  lock", Vilo)
        lock = true

        direction = newDirection
        if (direction != tank.vect) {
          tank.changeDirection(direction) {
            () => self ! UnLock
          }
        } else {
          tank.move( direction) {
            () => self ! UnLock
          }
        }
      } else {
        debug("TankActor: message is ignoring " + newDirection, All)
      }

    case UnLock => //when one key si released, actor needs to continue
      lock = false
      debug("TankActor: unlock actor" + direction, Vilo)

    case m@AnyRef => warn("RotationActor : Unknow message = " + m, All)
  }
}
