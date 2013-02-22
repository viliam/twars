package sk.kave.tank.actors

import sk.kave.tank.events.Event
import scalafx.util.Duration
import scalafx.beans.property.Property
import javafx.event.{ActionEvent, EventHandler}
import scalafx.Includes._
import sk.kave.tank.Main
import javafx.animation.{KeyFrame, Timeline, KeyValue}
import javafx.beans.value.WritableValue

class TimelineActor extends JfxActor{

  def act() {
    link( Main.controlerActor)

    react {
      case TimelineMessage(event, duration, trf) => {
        val timeline = new Timeline()


        for ( (p,v) <- trf ) {
          val kv = new KeyValue(p, v)
          val kf = new KeyFrame(duration, kv)
          timeline.getKeyFrames().add(kf)
        }


        timeline.onFinished = new EventHandler[ActionEvent] {
            def handle(e: ActionEvent) {
              Main.controlerActor ! event
            }
          }

        timeline.play()
        act()
      }
      case _ => act()
    }
  }
}

abstract class Messages
case class TimelineMessage[T]( e : Event, duration : Duration, trf : List[ (WritableValue[T], T) ] ) extends Messages
