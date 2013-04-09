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

package sk.kave.tank.beans

import sk.kave.tank._
import events._
import events.TankMoveEvent
import events.TankRotationEvent
import sk.kave.tank.fx._
import scala.Some

class TankImpl (
            @volatile private var _x: Int,
            @volatile private var _y: Int,
            @volatile private var _direction : Vector2D = (None, Some(UP)))
          (implicit gContext : IGameContext) extends Tank {

  import gContext._
  import gContext.config._

  def x = _x
  def y = _y


  def direction = _direction

  def changeDirection(direction: Vector2D)(callBack: () => Unit) {
    val oldDirection = this.direction
    this._direction = direction

    fireEvent(TankRotationEvent(direction, oldDirection, callBack))
  }

  def cleanGround(): Int = {
    var groundCount = 0
    for (c <- x until x + tankSize;
         r <- y until y + tankSize) {
      if (map(c, r) == Ground) groundCount = groundCount + 1
      map(c, r) = Grass
    }
    groundCount
  }

  def canMove(vect: Vector2D) = map.canMove((x, y), (tankSize, tankSize), vect) && !isStoneAhead(vect)

  private def isStoneAhead(direction: Vector2D): Boolean = {
    val x = direction.horizontal match {
      case Some(RIGHT) => _x + 1
      case Some(LEFT) => _x - 1
      case None => _x
    }

    val y = direction.vertical match {
      case Some(DOWN) => _y + 1
      case Some(UP) => _y - 1
      case None => _y
    }

    for (c <- x until x + tankSize;
         r <- y until y + tankSize) {
      if (map(c, r) == Stone) return true
    }

    false
  }

  def move(vect: Vector2D)(callback: () => Unit) {
    if (!canMove(vect)) {
      debug("tank cannot move anymore", All)
      callback()
      return
    }

    vect.horizontal match {
      case Some(RIGHT) => _x = x + 1
      case Some(LEFT) => _x = x - 1
      case None =>
    }

    vect.vertical match {
      case Some(DOWN) => _y = y + 1
      case Some(UP) => _y = y - 1
      case None =>
    }

    val groundCleaned = cleanGround()
    debug("Tank cleared ground " + groundCleaned, Igor)

    fireEvent(TankMoveEvent(this.x, this.y, vect, callback))
  }

  override def shoot(callback: () => Unit) {
    val bulletPos = getInitBulletPosition()

    map.shoot ( ShootEvent(bulletPos._1,bulletPos._2, new Bullet( direction), callback) )
  }

  protected def getInitBulletPosition():(Double,Double)={

    /**
     * calculates bullet shift according to tank rotation
     * this will create an effect of shooting from the tank's barrel
     */
    def calculateBulletShift(tankAngle:Double):(Double,Double)={
      val x = tankSize/2 * math.cos(math.toRadians(tankAngle))
      val y = tankSize/2 * math.sin(math.toRadians(tankAngle))
      (math.signum(math.round(x)), math.signum(math.round(y)))
    }

    val tankCenter = (this.x + tankSize/2, this.y + tankSize/2)
    val bulletShift = calculateBulletShift(Tank.getAngleFull((Some(RIGHT), None),direction))

    (tankCenter._1 + bulletShift._1/2,tankCenter._2 + bulletShift._2/2)
  }
}
