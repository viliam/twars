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

//object Direction extends Enumeration {
//  val DOWN, LEFT, RIGHT, UP, NONE = Value
//}
trait Direction

trait Vertical extends Direction
object LEFT extends Vertical
object RIGHT extends Vertical

trait Horizontal extends Direction
object DOWN extends Horizontal
object UP extends Horizontal

object NoneDir extends  Direction with Horizontal with Vertical


class GameControlerActor(val mapGroup: Group) extends Actor {
  self =>

  var direction: (Horizontal, Vertical) = (   NoneDir, NoneDir)
  var (horizontal, vertical) = direction

  def act() {
    react {
      case (Action.EXIT) => ()
      case a: Action.Value =>
        runInJFXthred(move(a))
        act()
    }
  }

  private def isMoving: Boolean = horizontal != NoneDir && vertical != NoneDir


  private def getDirectionHorizontal  =
    horizontal match {
      case LEFT => +ItemSize
      case RIGHT => -ItemSize
      case NoneDir => 0
    }

  private def getDirectionVertical =
    horizontal match {
      case UP => +ItemSize
      case DOWN => -ItemSize
      case NoneDir => 0
    }


  private def move(action: Action.Value) {


    action match {
      case Action.UP =>
        vertical = UP
        mapGroup.translateY

      case Action.LEFT =>
        horizontal = LEFT
        mapGroup.translateX

      case Action.RIGHT =>
        horizontal = RIGHT
        mapGroup.translateX

      case Action.DOWN =>
        vertical = DOWN
        mapGroup.translateY
    }

    if (!isMoving) {
      //isMoving = true

      new Timeline() {
        onFinished = new EventHandler[ActionEvent] {
          def handle(e: ActionEvent) {
            //isMoving = false
          }
        }


//        keyFrames = Seq(
//          at(0 ms) {
//            Set(geTranslateX -> geTranslateX(),
//                geTranslateY -> geTranslateY())
//          },
//          at(10 ms) {
//            Set(geTranslateX -> (geTranslateX() + getDirectionHorizontal),
//                geTranslateY -> (geTranslateX() + getDirectionVertical))
//          }
//        )
      }.play
    }

  }

  private def runInJFXthred(runThis: => Unit) {
    javafx.application.Platform.runLater(new Runnable() {
      def run() = runThis
    })
  }
}


