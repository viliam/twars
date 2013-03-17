package sk.kave.tank.actors

import sk.kave.tank.events.Event
import scalafx.util.Duration
import javafx.beans.value.WritableValue
import sk.kave.tank.fx.Direction
import sk.kave.tank._

object KeyPressEvent extends Enumeration {
  val PRESSED, RELEASED = Value
}

sealed abstract class ActorMessage

case class TimelineMessage[T]( duration : Duration,
                               trf : List[ (WritableValue[T], T) ],
                               callback : () => Unit ) extends ActorMessage

case class ChangeMovement( direction : Direction, keyPress : KeyPressEvent.Value) extends ActorMessage
case class UnLockMoving() extends ActorMessage
case class ContinueMovement() extends ActorMessage

case class Shoot(keyPress : KeyPressEvent.Value) extends ActorMessage