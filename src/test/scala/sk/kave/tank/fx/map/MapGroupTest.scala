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

package sk.kave.tank.fx.map

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.helpers.{ReflectionHelper, GameTestContext}
import org.scalatest.mock.MockitoSugar
import sk.kave.tank.fx.{DOWN, LEFT}
import sk.kave.tank._
import events.TankMoveEvent
import scala.Some
import org.specs2.mock.Mockito


/**
 * @autor vilo & igor
 */

class MapGroupTest extends FlatSpec with MockitoSugar with ShouldMatchers with ReflectionHelper with BeforeAndAfterEach with Mockito {

  implicit object gC extends GameTestContext

  var mapGroup: MapGroup = null

  override def beforeEach() {
    //    val mapGroup = spy(new MapGroup())
    //    mapGroup.canMapMove((Some(LEFT),Some(DOWN))) returns(true)

//    mapGroup = new MapGroup() with TestMapMovement



//    mapGroup.setPrivateVar("timelineActor", mock)
  }


  "Only map " should " move" in {
    //    val mapGroup = mock[MapGroup]
    //      when(mapGroup. (0,0)).thenReturn(Ground)
    val e = TankMoveEvent(2, 3, (Some(LEFT), Some(DOWN)), () => {})
    mapGroup.callPrivateMethod("moveMap", e)
  }
//}
//
//trait TestMapMovement extends MapGroup {
//
//  override def canMapMove(tuple: Vector2D): Boolean = true
//
//  override lazy val timelineActor: ActorRef = {
//    implicit val actorSystem = ActorSystem("TankActorSystem")
//    val probe = TestProbe()
//    probe.expectMsgClass(classOf[TimelineMessage[Number]])
//
//     probe.ref
//  }
}
