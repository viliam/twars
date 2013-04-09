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

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.fx.{DOWN, RIGHT, LEFT, UP}
import sk.kave.tank.helpers.{EventOccurredException, TestEventListener, ReflectionHelper}
import sk.kave.tank.events.{MapEvent, ShootEvent, MapChangeEvent}
import sk.kave.tank.utils.Vector2D
import scala.Some

/**
 * @author Igo
 */
class MapImplTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach with ReflectionHelper {

  var mapInstance: Map = null

  override def beforeEach() {
    mapInstance = new MapImpl(Array(
      Array(Stone, Grass, Ground),
      Array(Grass, Stone, Ground),
      Array(Stone, Ground, Grass))) with TestEventListener[MapEvent]
  }


  "Map " should " not get value" in {
    mapInstance(0, 3) should equal(NoMap)
    mapInstance(0, -1) should equal(NoMap)
    mapInstance(3, 0) should equal(NoMap)
    mapInstance(-1, 0) should equal(NoMap)
  }

  "Map " should " get value" in {
    mapInstance(2, 2) should equal(Grass)
    mapInstance(0, 0) should equal(Stone)
    mapInstance(1, 1) should equal(Stone)
    mapInstance(1, 2) should equal(Ground)
  }


  "Map " should " fire event when changed" in {

    val mE = intercept[EventOccurredException[MapChangeEvent]] {
      mapInstance(0, 1) = Stone
    }.event

    mE should not be null
    mE.row should equal(1)
    mE.col should equal(0)
    mE.newValue should be(Stone)
  }

  "Map " should " update when changed" in {

    //change map
    mapInstance(0, 1) should not be (Stone) //before map change
    intercept[EventOccurredException[MapChangeEvent]] {
      mapInstance(0, 1) = Stone
    }
    mapInstance(0, 1) should be(Stone)
  }

  "Map " should "check if move is possible" in {
    mapInstance.canMove((0, 0), (1, 1), (Some(RIGHT), None)) should be(true)
    mapInstance.canMove((0, 0), (1, 1), (Some(LEFT), None)) should be(false)

    mapInstance.canMove((3, 0), (1, 1), (Some(LEFT), None)) should be(true)
    mapInstance.canMove((3, 0), (1, 1), (Some(RIGHT), None)) should be(false)

    mapInstance.canMove((0, 0), (1, 1), (None, Some(DOWN))) should be(true)
    mapInstance.canMove((0, 0), (1, 1), (None, Some(UP))) should be(false)

    mapInstance.canMove((0, 3), (1, 1), (None, Some(DOWN))) should be(false)
    mapInstance.canMove((0, 3), (1, 1), (None, Some(UP))) should be(true)
  }

  "Shoot event " should " be fired" in {
    //it would be also nice to test if bullet event invokes new bullet event - but I cannot do it without the problem in line above

    val shootEvent = ShootEvent(1, 2, new Bullet((Some(LEFT), Some(UP))), () => {})

    val sE = intercept[EventOccurredException[ShootEvent]] {
      mapInstance.shoot(shootEvent)
    }.event

    sE.x should equal(1)
    sE.y should equal(2)
    sE.bullet should not be null
    sE.bullet.direction should be((Some(LEFT), Some(UP)): Vector2D)
  }

  "Bullet " should " check if move is possible" in {
    //todo finish when canBulletMove() will no longer work with Doubles, but Ints instead
    //mapInstance.callPrivateMethod("canBulletMove",)
  }
}
