/*
 * Copyright
 *  viliam.kois@gmail.com   Kois Viliam
 *  ikvaso@gmail.com        Kvasnicka Igor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package sk.kave.tank.actors


import sk.kave.tank._
import beans.GameContextImpl
import akka.actor.Actor
import utils.Logger
import sk.kave.tank.events.CreateNewBullet

/**
 * actor performing movement of tank.
 *
 * @author Vil
 */
class TankActor extends Actor with Logger {

  val gContext = implicitly[GameContextImpl]
  import gContext._

  //private var direction: Vector2D = initG.direction
  private var lockMove = false
  private var lockShoot = false

  private var direction: Vector2D = (None, None)

  def receive = {
    case ChangeMovement(a, kpe) =>
      if ( checkDirection( direction.updateDirection(a, kpe)) ) {
        move(direction)
      }
    case ContinueMovement => move(direction)
    case UnLockMoving => unlockMove()
    case Shoot( KeyPressEvent.PRESSED) => startShooting()
    case Shoot( KeyPressEvent.RELEASED) => {
      tank.lastCreatedBullet = None
      lockShoot = false
    }
    case CreateNewBullet(callback) => if (lockShoot) tank.shoot(callback)
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
        tank.changeDirection(direction) {  () => { self ! UnLockMoving } }
      } else {
        tank.move(direction) {  () => self ! UnLockMoving  }
      }
    } else {
      debug("TankActor: message is ignoring " + direction, All)
    }
  }

  def unlockMove() {
    debug("TankActor: unlock move", Vilo)
    lockMove = false
    if (tank.direction.isDefined) {
      move(direction)
    }
  }

  def startShooting() {
   if (!lockShoot) {
     lockShoot = true
     tank.shoot  {  () => () }
   }
  }

}
