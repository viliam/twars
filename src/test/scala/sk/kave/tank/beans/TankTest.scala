package sk.kave.tank.beans

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import junit.JUnitRunner
import org.junit.runner.RunWith
import scala.Predef._
import sk.kave.tank._
import fx.{RIGHT, LEFT, UP}
import scala.Some

/**
 * @autor : wilo
 */

@RunWith(classOf[JUnitRunner])
class TankTest extends FlatSpec with ShouldMatchers {

  implicit val gTestContext = new GameTestContext

  "A tank " should " calculate shortest rotation" in {
    import scala.collection.immutable.Map
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

  it should " calculate full rotation " in {
     //todo: Tank.getFullAngle
  }

  it should " inspect if it is in position" in {
    Tank.isInPosition(5, 5)  (gTestContext) should equal ((true,  true))
    Tank.isInPosition(799, 5)(gTestContext) should equal ((false, true))
    Tank.isInPosition(2, 5)  (gTestContext) should equal ((false, true))
    Tank.isInPosition(5, 1)  (gTestContext) should equal ((true, false))
    Tank.isInPosition(5, 799)(gTestContext) should equal ((true, false))
  }

}
