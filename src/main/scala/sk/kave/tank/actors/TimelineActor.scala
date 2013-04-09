/*
 * Copyright
 *  viliam.kois@gmail.com   Kois Viliam
 *  ikvaso@gmail.com        Kvasnicka Igor
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

package sk.kave.tank.actors

import sk.kave.tank.events.Event
import scalafx.util.Duration
import javafx.event.{ActionEvent, EventHandler}
import scalafx.Includes._
import sk.kave.tank.Main
import javafx.animation.{KeyFrame, Timeline, KeyValue}
import javafx.beans.value.WritableValue
import akka.actor.Actor
import scalafx.application.Platform

@Deprecated
//I'm not sure, but we don't need new actor for timeline
//I'm thinking, run timeline directly from jx-thread is save enough
class TimelineActor extends Actor {

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("in preRestart hook")
  }

  def receive = {
    case TimelineMessage(duration, trf, callback) =>
      Platform.runLater {
        val timeline = new Timeline()

        for ( (p,v) <- trf ) {
          val kv = new KeyValue(p, v)
          val kf = new KeyFrame(duration, kv)
          timeline.getKeyFrames().add(kf)
        }

        timeline.onFinished = new EventHandler[ActionEvent] {
            def handle(e: ActionEvent) {
              callback()
            }
          }

         timeline.play()
      }
    case e @ _ => println("Unknown message : "  + e)
  }

//  def runJfx( run : => Unit) =
//    javafx.application.Platform.runLater(new Runnable() {
//            def run() = run
//          })
}