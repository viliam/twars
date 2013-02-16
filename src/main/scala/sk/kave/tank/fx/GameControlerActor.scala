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
import scalafx.animation.Timeline
import javafx.event.EventHandler
import javafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.Group
import sk.kave.tank._
import sun.awt.VerticalBagLayout

/*

This actor will be responsible for handling events. This asynchronized processing of events make processing events
synchronized.

 */

object Action extends Enumeration {
  val DOWN, LEFT, RIGHT, UP, EXIT = Value
}

class GameControlerActor(val mapGroup: Group) extends Actor {
  self =>

  var direction: (Option[Horizontal], Option[Vertical]) = (None, None)
  var (horizontal, vertical) = direction

  def act() {
    react {
      case (Action.EXIT, _) =>
        logg.info("actor says 'Good bye'")
      case (a: Action.Value, kpe: KeyPressEvent.Value) =>
        updateDirection(a, kpe)
        runInJFXthread(move())
        act()
    }
  }

  private def isMoving: Boolean = !horizontal.isEmpty || !vertical.isEmpty


  private def getDirectionHorizontal =
    horizontal match {
      case Some(LEFT) => +ItemSize
      case Some(RIGHT) => -ItemSize
      case None => 0
    }

  private def getDirectionVertical =
    vertical match {
      case Some(UP) => +ItemSize
      case Some(DOWN) => -ItemSize
      case None => 0
    }

  private def translateX = mapGroup.translateX
  private def translateY = mapGroup.translateY

  private def setAction[T <: Direction](newDirection: T, kpe: KeyPressEvent.Value): Option[T] = {
    if (kpe == KeyPressEvent.RELEASED) {
      None
    } else {
      Some(newDirection)
    }
  }

  private def updateDirection(action: Action.Value, kpe: KeyPressEvent.Value) {
    action match {
      case Action.UP =>
        vertical = setAction( UP, kpe)

      case Action.DOWN =>
        vertical = setAction(DOWN, kpe)

      case Action.LEFT =>
        horizontal = setAction(LEFT, kpe)

      case Action.RIGHT =>
        horizontal = setAction(RIGHT, kpe)
    }
  }

  var isTimelineAlive = false
  private def move() {
    if (!isMoving && !isTimelineAlive) {
      return
    }

    isTimelineAlive = true
    new Timeline() {
      onFinished = new EventHandler[ActionEvent] {
        def handle(e: ActionEvent) {
          if (isMoving) move()
          else isTimelineAlive = false
        }
      }

      keyFrames = Seq(
        at(0 ms) {
          Set(translateX -> translateX(),
            translateY -> translateY())
        },
        at(10 ms) {
          Set(translateX -> (translateX() + getDirectionHorizontal),
            translateY -> (translateY() + getDirectionVertical))
        }
      )
    }.play
  }

  private def runInJFXthread(runThis: => Unit) {
    javafx.application.Platform.runLater(new Runnable() {
      def run() {
        runThis
      }
    })
  }
}


