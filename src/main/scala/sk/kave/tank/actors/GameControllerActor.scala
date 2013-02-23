package sk.kave.tank.actors

import sk.kave.tank._
import events.TankRotationEvent
import sk.kave.tank.fx._
import sk.kave.tank.fx.map.KeyPressEvent
import akka.actor.{Props, Actor}
import scala.Some

class GameControllerActor extends Actor {

  private var (horizontal, vertical): Vector2D = (None, None)

  private val rotationActor = context.actorOf( Props[RotationActor])
  val timelineActor = context.actorOf( Props[TimelineActor]) //.withDispatcher("javafx-dispatcher"))

  def receive = {
    case (Action.EXIT, _) =>
      logg.info("actor says 'Good bye'")
      context.stop(self)
    case (a: Action.Value, kpe: KeyPressEvent.Value) =>
      updateDirection(a, kpe)
      makeMove()
    case Action.CONTINUE =>
      if (isMoving) {
        makeMove()
      }
    case m @ TimelineMessage(_,_,_) =>
      timelineActor ! m
    case TankRotationEvent(_) =>
      rotationActor ! Action.CONTINUE
    case m @ AnyRef => logg.debug("GameControllerActor : Unknow message = " + m)
  }


  private def makeMove() {
    rotationActor !(horizontal, vertical)
  }

  private def isMoving: Boolean = horizontal.isDefined || vertical.isDefined

  private def updateDirection(action: Action.Value, kpe: KeyPressEvent.Value) {

    def setAction[T <: Direction](newDirection: T, oldDirection: Option[T], kpe: KeyPressEvent.Value): Option[T] = {
      if (kpe == KeyPressEvent.RELEASED && oldDirection.isDefined && oldDirection.get == newDirection) {
        None
      } else {
        Some(newDirection)
      }
    }

    action match {
      case Action.UP =>
        vertical = setAction(UP, vertical, kpe)

      case Action.DOWN =>
        vertical = setAction(DOWN, vertical, kpe)

      case Action.LEFT =>
        horizontal = setAction(LEFT, horizontal, kpe)

      case Action.RIGHT =>
        horizontal = setAction(RIGHT, horizontal, kpe)
    }
  }
}

object Action extends Enumeration {
  val DOWN, LEFT, RIGHT, UP, EXIT, CONTINUE = Value
}


