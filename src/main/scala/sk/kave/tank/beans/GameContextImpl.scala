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
import actors.TankActor
import fx.UP
import akka.actor.{Props, TypedActor, TypedProps}

trait GameContextImpl extends IGameContext {

  lazy val tankActor = Main.system.actorOf(Props[TankActor])
  lazy val map : Map = TypedActor( Main.system).typedActorOf( TypedProps( classOf[Map], Map("mapaGround.mapa")), "map")

  val (mapWidth, mapHeight) : (Int, Int) = map.bound

  val initG : IGameInit = new IGameInit {
    override val tankX = mapWidth /2
    override val tankY = mapHeight /2
    override val direction = (None, Some(UP))
  }

  override val config :Config = ConfigImpl

  lazy val tank : Tank = TypedActor(Main.system).typedActorOf(TypedProps(classOf[Tank],
                            new TankImpl(initG.tankX,initG.tankY)), "tank")


}
