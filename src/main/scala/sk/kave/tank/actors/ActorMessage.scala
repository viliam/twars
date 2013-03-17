package sk.kave.tank.actors

import sk.kave.tank.events.Event
import scalafx.util.Duration
import javafx.beans.value.WritableValue
import sk.kave.tank.fx.Direction
import sk.kave.tank._

object KeyPressEvent extends Enumeration {
  val PRESSED, RELEASED = Value
}

abstract class ActorMessage

case class Exit() extends ActorMessage
case class UserMessage( direction : Direction, keyPress : KeyPressEvent.Value) extends ActorMessage
case class UnLock() extends ActorMessage
case class TimelineMessage[T]( duration : Duration,
                               trf : List[ (WritableValue[T], T) ],
                               callback : () => Unit ) extends ActorMessage
case class NewDirection( direction : Vector2D) extends ActorMessage
case class ContinueMovement extends ActorMessage