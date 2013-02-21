package sk.kave.tank.actors

import actors.Actor
import sk.kave.tank._
import sk.kave.tank.fx._
import sk.kave.tank.fx.map.KeyPressEvent
import scalafx.scene.Group

class GameControllerActor(val mapGroup: Group) extends Actor {
  self =>

  var (horizontal, vertical): Vector2D = (None, None)

  val tankActor = (new TankActor).start()

  private var isTimelineAlive = false //private lock used for waiting for finish timeline moving

  def act() {
    react {
      case (Action.EXIT, _) =>
        logg.info("actor says 'Good bye'")
        exit()
      case (a: Action.Value, kpe: KeyPressEvent.Value) =>
        updateDirection(a, kpe)

        if (!isTimelineAlive) {
          makeMove()
        }
        act()

      case Action.CONTINUE =>
          isTimelineAlive = false
        if (isMoving) {
          makeMove()
        }
        act()
    }
  }

  private def makeMove() {
    isTimelineAlive = true
    tankActor !(horizontal, vertical)
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