package sk.kave.tank.beans

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.helpers.{TestEventListener, GameTestContext}
import sk.kave.tank.events.TankEvent

/**
 * @autor : vilo
**/
class TankImplTest extends FlatSpec with ShouldMatchers {

  implicit val gTestContext = new GameTestContext

  "A tank " should " change direction and then fires event " in {
    implicit val gTestContext = new GameTestContext {
      // not necessary to wrap tank in gContext,. I can test it directly,.. it'll rewrite
      override lazy val tank = new TankImpl(10,10) with TestEventListener[TankEvent]

    }
  }

}