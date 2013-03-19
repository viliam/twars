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

case class TankRotationEvent(newDirection : Vector2D, oldDirection: Vector2D, callback: () => Unit) extends TankEvent

case class TankMoveEvent(x: Int, y: Int, direction:Vector2D, callback: () => Unit) extends TankEvent{
  override def toString:String={
    "TankMoveEvent: x = " + x + ", y = " + y
  }
}

