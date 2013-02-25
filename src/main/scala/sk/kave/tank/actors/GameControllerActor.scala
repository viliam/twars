package sk.kave.tank.actors

import sk.kave.tank._
import sk.kave.tank.fx.{Direction, Vertical, Horizontal}
import akka.actor.{Props, Actor}
import scala.Some

class GameControllerActor extends Actor {

  private var (horizontal, vertical): Vector2D = (None, None)

  private lazy val tankActor = context.actorOf( Props[TankActor])
  private lazy val timelineActor = context.actorOf( Props[TimelineActor]) //.withDispatcher("javafx-dispatcher"))

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("in preRestart hook")
  }

  def receive = {
    case Exit  =>
      logg.info("actor says 'Good bye'")
      context.stop( self)
      context.system.shutdown()

    case UserMessage(a, kpe) =>
      updateDirection(a, kpe)
      if (isMoving) tankActor ! NewDirection(horizontal, vertical)

    case m @ TimelineMessage(_,_,_) =>
      timelineActor ! m
  }

  private def isMoving: Boolean = horizontal.isDefined || vertical.isDefined

  private def updateDirection(direction: Direction, kpe: KeyPressEvent.Value) {

    def setAction[T <: Direction](newDirection: T, oldDirection: Option[T], kpe: KeyPressEvent.Value): Option[T] = {
      if (kpe == KeyPressEvent.RELEASED && oldDirection.isDefined && oldDirection.get == newDirection) {
        None
      } else {
        Some(newDirection)
      }
    }

    direction match {
      case v : Vertical =>
        vertical = setAction(v, vertical, kpe)
      case h : Horizontal =>
        horizontal = setAction(h, horizontal, kpe)
    }
  }
}

