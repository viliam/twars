package sk.kave.tank.events

import sk.kave.tank._
/**
 * @author Igo
 */
trait EventListener[E <: Event] {

  private var listenerMap = Map[Any, (E => Unit)]()

  def addListener(listener: Any, callback: (E => Unit)) {
    listenerMap += (listener -> callback)
  }

  def removeListener(listener: Any) {
    listenerMap -= listener
  }

  def fireEvent(event: E) {
    logg.debug( "Fire event : " + event)
    listenerMap.values.foreach(call => call(event))
  }
}
