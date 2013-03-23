package sk.kave.tank.helpers

import sk.kave.tank.events.{Event, EventListener}

/**
 * @autor : vilo
 */

class EventOccurredException[E <: Event](val event : E) extends Exception

trait TestEventListener[E <: Event] extends EventListener[E] {

  override def fireEvent(event: E) = throw new EventOccurredException(event)

}
