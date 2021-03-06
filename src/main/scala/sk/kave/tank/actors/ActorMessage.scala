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

package sk.kave.tank.actors

import sk.kave.tank.events.Event
import scalafx.util.Duration
import javafx.beans.value.WritableValue
import sk.kave.tank.fx.Direction
import sk.kave.tank._

object KeyPressEvent extends Enumeration {
  val PRESSED, RELEASED = Value
}

sealed abstract class ActorMessage

case class ChangeMovement( direction : Direction, keyPress : KeyPressEvent.Value) extends ActorMessage
case class UnLockMoving() extends ActorMessage
case class ContinueMovement() extends ActorMessage

case class Shoot(keyPress : KeyPressEvent.Value) extends ActorMessage