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
  }

  def runJfx( run : => Unit) =
    javafx.application.Platform.runLater(new Runnable() {
            def run() = run
          })
}