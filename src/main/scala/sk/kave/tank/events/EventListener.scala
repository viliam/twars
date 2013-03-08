package sk.kave.tank.events

import sk.kave.tank._
import utils.Logger

/**
 * @author Igo
 */
trait EventListener[E <: Event] extends Logger {

  private var listenerMap = Map[Any, (E => Unit)]()

  def addListener(listener: Any, callback: (E => Unit)) {
    listenerMap += (listener -> callback)
  }

  def removeListener(listener: Any) {
    listenerMap -= listener
  }

  //todo fix me:!! event is running in the same thread like actor
  //      - be carefull for deadlocks
  def fireEvent(event: E) {
    debug( "Fire event : " + event, Vilo)
    listenerMap.values.foreach(call => call(event))
  }
}
