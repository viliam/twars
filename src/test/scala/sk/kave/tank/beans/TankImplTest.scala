package sk.kave.tank.beans

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.helpers.{EventOccurredException, TestEventListener, GameTestContext}
import sk.kave.tank._
import events.{TankEvent, TankRotationEvent}
import fx.UP
import scala.Some
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._


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
    when(mapMock.bound).thenReturn( (2,2))
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

}