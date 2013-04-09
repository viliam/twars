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

package sk.kave.tank.beans

import sk.kave.tank._
import akka.actor.ActorRef
import sk.kave.tank.{ConfigImpl, Config}

/**
 * User: vilo
 */
trait IGameContext {

  trait IGameInit {
    def tankX : Int
    def tankY : Int
    def direction : Vector2D
  }

  def tankActor : ActorRef
  def map : Map

  def mapWidth : Int
  def mapHeight: Int

  def initG : IGameInit

  val config : Config = ConfigImpl

  def tank : Tank
}
