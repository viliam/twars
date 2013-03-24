package sk.kave.tank.beans

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.helpers.{EventOccurredException, TestEventListener, GameTestContext}
import sk.kave.tank._
import events.{TankEvent, TankRotationEvent}
import fx.UP
import scala.Some
import org.scalamock.scalatest.MockFactory

/**
 * @autor : vilo
**/
class TankImplTest extends FlatSpec with MockFactory with ShouldMatchers  {

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
//    val map = mock[Map]
//    map expects 'apply withArgs args(0,0) returning Ground
//    map expects 'apply withArgs args(0,1) returning Grass
//    map expects 'apply withArgs args(1,0) returning Grass
//    map expects 'apply withArgs args(1,1) returning Ground
//
//    map expects 'update withArgs args(0,0, Grass)
//    map expects 'update withArgs args(1,1, Grass)
//
//    val tank = new TankImpl(10,10)( new GameTestContext)
//    tank.clearGroup should equal (2)
  }

}