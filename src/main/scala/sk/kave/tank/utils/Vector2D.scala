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

package sk.kave.tank.utils

import sk.kave.tank._
import actors.KeyPressEvent
import fx._
import scala.Some

/**
 * @author Igo
 */
case class Vector2D(value: (Option[Horizontal], Option[Vertical])) {
  val horizontal = value._1
  val vertical = value._2

  def apply(): (Option[Horizontal], Option[Vertical]) = value

  /**
   * calculates shift of a object according to Vector2D value
   * @return
   */
  def getShift(px : Int) : (Int, Int) =
    (
      value._1 match {
        case Some(LEFT)  => +px
        case Some(RIGHT) => -px
        case None => 0
      }
      ,
      value._2 match {
        case Some(UP)   => +px
        case Some(DOWN) => -px
        case None => 0
      }
      )

  def getShift( x: Double, y: Double) : (Double, Double) =
    (
      value._1 match {
        case Some(LEFT)  => x +1
        case Some(RIGHT) => x -1
        case None => x
      }
      ,
      value._2 match {
        case Some(UP)   => y-1
        case Some(DOWN) => y+1
        case None => y
      }
      )

  /**
   * at least one direction is defined
   * @return
   */
  def isDefined: Boolean = value._1.isDefined || value._2.isDefined

  def updateDirection(direction: Direction, kpe: KeyPressEvent.Value): Vector2D = {

      def setAction[T <: Direction](newDirection: T, oldDirection: Option[T], kpe: KeyPressEvent.Value): Option[T] = {
        if (kpe == KeyPressEvent.RELEASED && oldDirection.isDefined && oldDirection.get == newDirection) {
          None
        } else {
          Some(newDirection)
        }
      }

      direction match {
        case v: Vertical   => Vector2D( (horizontal, setAction(v, vertical, kpe)) )
        case h: Horizontal => Vector2D( (setAction(h, horizontal, kpe), vertical) )
      }
    }

}

