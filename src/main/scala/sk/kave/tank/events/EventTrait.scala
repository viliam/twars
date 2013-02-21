package sk.kave.tank.events


/**
 * @author Igo
 */
trait EventTrait[E] {
  private var listenerMap = Map[Any, (E => Unit)]()


  def addListener(listener: Any, callback: (E => Unit)) {
    listenerMap += (listener -> callback)
  }

  def removeListener(listener: Any) {
    listenerMap -= listener
  }

  def fireEvent(event: E) {
    listenerMap.values.foreach(call => call(event))
  }
}
