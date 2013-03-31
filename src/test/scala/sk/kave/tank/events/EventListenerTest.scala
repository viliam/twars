package sk.kave.tank.events

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.helpers.ReflectionHelper
import akka.actor.ActorRef
import org.scalatest.mock.MockitoSugar

/**
 * @author Igo
 */
class EventListenerTest extends FlatSpec with MockitoSugar with ShouldMatchers with ReflectionHelper {

  class TestEventListenerObject extends EventListener[TankEvent]


  "Duplicate listener " should " not be allowed to register" in {
    val testEventListener = new TestEventListenerObject
    testEventListener.addListener(1, null)

    intercept[IllegalArgumentException] {
      testEventListener.addListener(1, null)
    }

    val listenerMap = testEventListener.getPrivateField("listenerMap", classOf[Map[Any, ActorRef]])
    listenerMap should have size (1)
  }

  "Listener " should " be registered" in {
    val testEventListener = new TestEventListenerObject
    testEventListener.addListener(1, null)
    testEventListener.addListener(2, null)

    val listenerMap = testEventListener.getPrivateField("listenerMap", classOf[Map[Any, ActorRef]])
    listenerMap should have size (2)
  }

  "Listener " should " be un-registered" in {
    val testEventListener = new TestEventListenerObject
    testEventListener.addListener(1, null)
    testEventListener.addListener(2, null)

    testEventListener.removeListener(1)

    val listenerMap = testEventListener.getPrivateField("listenerMap", classOf[Map[Any, ActorRef]])
    listenerMap should have size (1)
  }

  "Unknown listener " should " not be un-registered" in {
      val testEventListener = new TestEventListenerObject
      testEventListener.addListener(1, null)
      testEventListener.addListener(2, null)

      testEventListener.removeListener(3)

      val listenerMap = testEventListener.getPrivateField("listenerMap", classOf[Map[Any, ActorRef]])
      listenerMap should have size (2)
    }
}
