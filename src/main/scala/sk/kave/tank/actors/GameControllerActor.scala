  package sk.kave.tank.actors

import sk.kave.tank._
import akka.actor.{Props, Actor}
import utils.Logger

class GameControllerActor extends Actor with Logger {

  private var direction: Vector2D = (None, None)

  private lazy val tankActor = context.actorOf(Props[TankActor])
//  private lazy val timelineActor = context.actorOf(Props[TimelineActor])

  override def preRestart(reason: Throwable, message: Option[Any]) {
    debug("in preRestart hook", All)
  }

  def isNewDirection(newDirection: Vector2D): Boolean = {
    if (direction!= newDirection) {
      direction = newDirection
      true
    } else
      false
  }

  def receive = {
    case Exit =>
      info("actor says 'Good bye'", All)
      context.stop(self)
      context.system.shutdown()

    case UserMessage(a, kpe) =>
      if ( isNewDirection( direction.updateDirection(a, kpe)) ) {
        if (direction.isDefined ) tankActor ! NewDirection( direction)
      }

    case ContinueMovement =>
      if (direction.isDefined ) tankActor ! NewDirection(direction)

//    case m@TimelineMessage(_, _, _) =>
//      timelineActor ! m
  }

}

