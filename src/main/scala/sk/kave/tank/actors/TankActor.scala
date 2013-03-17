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

  val gContext = implicitly[Game]
  import gContext._

  //private var direction: Vector2D = initG.direction
  private var lockMove = false

  private var direction: Vector2D = (None, None)

  def receive = {
    case UserMessage(a, kpe) =>
      if ( checkDirection( direction.updateDirection(a, kpe)) ) {
        move(direction)
      }
    case ContinueMovement => move(direction)
    case UnLock => unlockMove()
    case m@AnyRef => warn("TankActor : Unknow message = " + m, All)
  }

  private def checkDirection(newDirection: Vector2D): Boolean = {
    if (direction!= newDirection) {
      direction = newDirection
      true
    } else
      false
  }

  def move(direction : Vector2D) = {
    if (direction.isDefined && !lockMove) {
      debug("TankActor:  lock", Vilo)
      lockMove = true

      if (direction != tank.direction) {
        tank.changeDirection(direction) {  () => { self ! UnLock } }
      } else {
        tank.move(direction) {  () => self ! UnLock  }
      }
    } else {
      debug("TankActor: message is ignoring " + direction, All)
    }
  }

  def unlockMove() {
    debug("TankActor: unlock", Vilo)
    lockMove = false
    if (tank.direction.isDefined) {
      move(direction)
    }
  }
}
