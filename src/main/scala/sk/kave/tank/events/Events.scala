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

package sk.kave.tank.events

import sk.kave.tank._
import beans.{Bullet, Items}

/**
 * @author Igo & Vil
 */
sealed abstract class Event

abstract class TankEvent extends Event
abstract class MapEvent extends Event

case class MapChangeEvent(col: Int, row: Int, newValue: Items) extends MapEvent
case class ShootEvent(x: Int, y: Int, bullet : Bullet, callback: () => Unit) extends MapEvent {
  override def toString:String={
    "ShootEvent: x = " + x + ", y = " + y + ", direction = " + bullet.direction
  }
}
case class BulletExplodedEvent(bullet:Bullet) extends MapEvent

case class TankRotationEvent(newDirection : Vector2D, oldDirection: Vector2D, callback: () => Unit) extends TankEvent

case class TankMoveEvent(x: Int, y: Int, direction:Vector2D, callback: () => Unit) extends TankEvent{
  override def toString:String={
    "TankMoveEvent: x = " + x + ", y = " + y
  }
}

