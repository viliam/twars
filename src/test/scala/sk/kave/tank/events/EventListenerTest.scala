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
