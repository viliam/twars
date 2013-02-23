package sk.kave.tank.events

import sk.kave.tank._
import sk.kave.tank.beans.Items

/**
 * @author Igo & Vil
 */
sealed abstract class Event

abstract class TankEvent extends Event

case class MapChangeEvent(col: Int,row: Int,newValue: Items) extends Event

case class TankRotationEvent( oldVector: Vector2D) extends TankEvent

case class TankMoveEvent( x : Int, y : Int) extends TankEvent