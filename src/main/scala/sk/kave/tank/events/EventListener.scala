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
    require(!listenerMap.contains(listener))
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
