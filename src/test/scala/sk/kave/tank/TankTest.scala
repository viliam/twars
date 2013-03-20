package sk.kave.tank

import beans.{GameTestContext, Tank}
import fx.{RIGHT, LEFT, UP}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import junit.JUnitRunner
import org.junit.runner.RunWith
/**
 * User: wilo
 */

@RunWith(classOf[JUnitRunner])
class TankTest extends FlatSpec with ShouldMatchers {

  implicit val gTestContext = new GameTestContext

  "A tank " should "rotate" in {
    val testData : Map[ (Vector2D, Vector2D), Int] = Map(

      ((None, Some(UP)), (Some(LEFT), Some(UP))) -> -45,
      ((Some(LEFT), Some(UP)), (None, Some(UP))) -> 45 ,
      ((None, Some(UP)), (Some(LEFT), None))     -> -90,
      ((Some(LEFT), None), (None, Some(UP)))     -> 90 ,

      ((None, Some(UP)), (Some(RIGHT), Some(UP)))-> 45 ,
      ((Some(RIGHT), Some(UP)), (None, Some(UP)))-> -45,
      ((None, Some(UP)), (Some(RIGHT), None))    -> 90 ,
      ((Some(RIGHT), None), (None, Some(UP)))    -> -90
    )

    for ( d <- testData.keys) {
       val (f,t) = d
       Tank.getAngle(f, t).toInt should equal(testData(d))
    }
  }

  it should " inspect if it is in position" in {
    Tank.isInPosition(5, 5)  (gTestContext) should equal ((true,  true))
    Tank.isInPosition(799, 5)(gTestContext) should equal ((false, true))
    Tank.isInPosition(2, 5)  (gTestContext) should equal ((false, true))
    Tank.isInPosition(5, 1)  (gTestContext) should equal ((true, false))
    Tank.isInPosition(5, 799)(gTestContext) should equal ((true, false))
  }

}
