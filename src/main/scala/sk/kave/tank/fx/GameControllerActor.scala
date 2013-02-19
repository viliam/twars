/*
 * Copyright viliam.kois@gmail.com Kois Viliam
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

package sk.kave.tank.fx

import actors.Actor
import scalafx.scene.Group
import sk.kave.tank._

/*

This actor will be responsible for handling events. This asynchronized processing of events make processing events
synchronized.

 */

object Action extends Enumeration {
  val DOWN, LEFT, RIGHT, UP, EXIT, CONTINUE = Value
}

class GameControllerActor(val mapGroup: Group) extends Actor {
  self =>

  var (horizontal, vertical): (Option[Horizontal], Option[Vertical]) = (None, None)

  val movingActor = new MovingActor(self)
  movingActor.start()

  private var isTimelineAlive = false //private lock used for waiting for finish timeline moving

  def act() {
    react {
      case (Action.EXIT, _) =>
        logg.info("actor says 'Good bye'")
        movingActor !(Action.EXIT, KeyPressEvent.PRESSED)
      case (a: Action.Value, kpe: KeyPressEvent.Value) =>

        updateDirection(a, kpe)

        if (!isTimelineAlive) {
          isTimelineAlive = true
          movingActor !(horizontal, vertical)
        }
        act()

      case Action.CONTINUE =>
        isTimelineAlive = false
        act()
    }
  }


  private def updateDirection(action: Action.Value, kpe: KeyPressEvent.Value) {
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


  private def setAction[T <: Direction](newDirection: T, oldDirection: Option[T], kpe: KeyPressEvent.Value): Option[T] = {
    if (kpe == KeyPressEvent.RELEASED && oldDirection.get == newDirection) {
      None
    } else {
      Some(newDirection)
    }
  }
}


