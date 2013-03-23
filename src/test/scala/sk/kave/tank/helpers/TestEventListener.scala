package sk.kave.tank.helpers

import sk.kave.tank.events.{Event, EventListener}

/**
 * @autor : wilo
 */
trait TestEventListener[E <: Event] extends EventListener[E] {

  class EventOccurredException(event : E) extends Exception

  override def fireEvent(event: E) = throw new EventOccurredException(event)

}
