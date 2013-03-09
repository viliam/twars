package sk.kave.tank.events

import sk.kave.tank._
import utils.Logger
import akka.actor.{ActorRef, Props, Actor}

/**
 * @author Igo
 */
trait EventListener[E <: Event] extends Logger {

  private var listenerMap = Map[Any, ActorRef]()

  def addListener(listener: Any, callback: (E => Unit)) {
    val listenerActor = Main.system.actorOf( Props( new Actor {
      def receive = {
        case e : E => callback(e)
      }
    }))
    listenerMap += (listener -> listenerActor)
  }

  def removeListener(listener: Any) {
    listenerMap -= listener
  }

  def fireEvent(event: E) {
    debug( "Fire event : " + event, Vilo)
    listenerMap.values.foreach(call => call ! event)
  }
}
