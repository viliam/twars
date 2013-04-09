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

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.helpers.{EventOccurredException, TestEventListener, GameTestContext}
import sk.kave.tank._
import events.{TankEvent, TankRotationEvent}
import fx.{RIGHT, DOWN, UP}
import scala.Some
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.PrivateMethodTester.PrivateMethod
import org.scalatest.PrivateMethodTester._


/**
 * @autor : vilo
**/
class TankImplTest extends FlatSpec with MockitoSugar with ShouldMatchers  {

//  implicit val gTestContext = new GameTestContext

  "A tank " should " change direction and then fires event " in {
    val direction : Vector2D = (None, Some(UP))
    val newDirection : Vector2D = (None, Some(UP))
    val tank = new TankImpl(10,10, direction) with TestEventListener[TankEvent]
    val callback = () => println("nic")

    val eventEx : EventOccurredException[TankEvent] = intercept[EventOccurredException[TankEvent]] {
      tank.changeDirection( newDirection)(callback)
    }
    tank.direction should equal (newDirection)

    assert( eventEx.event.isInstanceOf[ TankRotationEvent] )
    val tankEvent = eventEx.event.asInstanceOf[ TankRotationEvent]

    tankEvent.newDirection should equal ( newDirection)
    tankEvent.oldDirection should equal ( direction)
    tankEvent.callback should equal ( callback)
  }

  it should " clean ground " in {
    val mapMock = mock[Map]
    when(mapMock(0,0)).thenReturn(Ground)
    when(mapMock(0,1)).thenReturn(Grass)
    when(mapMock(1,0)).thenReturn(Grass)
    when(mapMock(1,1)).thenReturn(Ground)

    val tank = new TankImpl(0,0)( new GameTestContext() {
      override lazy val map = mapMock
    })
    tank.cleanGround() should equal (2)

    verify(mapMock).update(0,0, Grass)
    verify(mapMock).update(1,1, Grass)
  }

  it should " recognize if stone is stone a head " in {
    val mapMock = mock[Map]
    when(mapMock(2,1)).thenReturn(Grass,Stone)
    when(mapMock(2,2)).thenReturn(Ground)
    when(mapMock(1,2)).thenReturn(Ground)
    when(mapMock(1,1)).thenReturn(Ground)

    val tank = new TankImpl(0,0, (Some(RIGHT), Some(DOWN)))( new GameTestContext() {
      override lazy val map = mapMock
    })

    val decorateIsStoneAhead = PrivateMethod[Boolean]('isStoneAhead)
    assert( ! (tank invokePrivate decorateIsStoneAhead( (Some(RIGHT), Some(DOWN)))) )
    assert(   (tank invokePrivate decorateIsStoneAhead( (Some(RIGHT), Some(DOWN)))) )
  }

  it should " shoot a bullet " in {
     //I am not sure, if it necessary to test this method
    // I must to look before, how to mock scala objects
  }

  it should " calculate initial position of bullet " in {

  }
}