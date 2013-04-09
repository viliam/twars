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

package sk.kave.tank.helpers

import scala.Predef._
import scala.Some
import sk.kave.tank.beans.Map
import sk.kave.tank.fx.UP
import sk.kave.tank.Config
import sk.kave.tank.beans.{TankImpl, Tank, IGameContext}

/**
 * User: wilo
 */
class GameTestContext extends IGameContext {

  override lazy val tankActor = null //Main.system.actorOf(Props[TankActor])
  override lazy val map : Map = Map("mapaGround.mapa")

  override val (mapWidth, mapHeight) : (Int, Int) = (10,10)

  override val initG : IGameInit = new IGameInit {
    override val tankX = mapWidth /2
    override val tankY = mapHeight /2
    override val direction = (None, Some(UP))
  }

  override lazy val tank : Tank = new TankImpl(initG.tankX,initG.tankY)

  override val config :Config = new Config() {
    val width = 10
    val height = 10
    val itemSize = 1
    val tankSize = 2
  }

}
