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

import sk.kave.tank.fx._
import sk.kave.tank._
import events.{TankEvent, EventListener}
import fx.{RIGHT, DOWN, LEFT}

object Tank {

  val transformation = Vector[Vector2D](//clockwise path
    (None, Some(UP)), (Some(LEFT), Some(UP)),
    (Some(LEFT), None), (Some(LEFT), Some(DOWN)),
    (None, Some(DOWN)), (Some(RIGHT), Some(DOWN)),
    (Some(RIGHT), None), (Some(RIGHT), Some(UP)))

  def getAngle(from: Vector2D, to: Vector2D): Double = {
    val i1 = transformation.indexOf(from)
    val i2 = transformation.indexOf(to)

    val n = (i1 - i2)

    //sometimes clockwise direction isn't shortest path
    if (math.abs(n) > transformation.size / 2) {
      (transformation.size - math.abs(n)) * math.signum(n) * -45
    }
    else {
      n * 45
    }
  }

  /**
   * calculates full angle
   */
  def getAngleFull(from: Vector2D, to: Vector2D): Double = {
    val i1 = transformation.indexOf(from)
    val i2 = transformation.indexOf(to)

    (i1 - i2) * 45
  }

  def isInPosition(x: Int, y: Int)(implicit gContext: IGameContext): (Boolean, Boolean) = {
    import gContext._
    import gContext.config._


    val h = if ((x > mapWidth- width / 2)  || (x < width / 2)) false else true
    val v = if ((y > mapHeight - height / 2) || (y < height / 2)) false else true

    (h, v)
  }
}


trait Tank extends EventListener[TankEvent] {

  def direction : Vector2D

  def changeDirection(direction: Vector2D)(callBack: () => Unit) : Unit

  def move(vect: Vector2D)(callback: () => Unit) : Unit

  def canMove(direction: Vector2D) : Boolean

  def shoot(callback: () => Unit)

  var lastCreatedBullet:Option[Bullet]
}
