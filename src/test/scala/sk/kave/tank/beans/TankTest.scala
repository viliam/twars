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

import org.scalatest._
import junit.JUnitRunner
import org.junit.runner.RunWith
import scala.Predef._
import sk.kave.tank._
import fx.{DOWN, RIGHT, LEFT, UP}
import helpers.GameTestContext
import scala.Some

/**
 * @autor : wilo
 */

@RunWith(classOf[JUnitRunner])
class TankTest extends FlatSpec with ShouldMatchers {

  implicit val gTestContext = new GameTestContext

  "A tank " should " calculate shortest rotation" in {
    import scala.collection.immutable.Map
    val testData: Map[(Vector2D, Vector2D), Int] = Map(

      ((None, Some(UP)), (Some(LEFT), Some(UP))) -> -45,
      ((Some(LEFT), Some(UP)), (None, Some(UP))) -> 45,
      ((None, Some(UP)), (Some(LEFT), None)) -> -90,
      ((Some(LEFT), None), (None, Some(UP))) -> 90,

      ((None, Some(UP)), (Some(RIGHT), Some(UP))) -> 45,
      ((Some(RIGHT), Some(UP)), (None, Some(UP))) -> -45,
      ((None, Some(UP)), (Some(RIGHT), None)) -> 90,
      ((Some(RIGHT), None), (None, Some(UP))) -> -90,
      ((Some(LEFT), Some(UP)), (Some(RIGHT), Some(DOWN))) -> -180
    )

    for (d <- testData.keys) {
      val (f, t) = d
      Tank.getAngle(f, t).toInt should equal(testData(d))
    }
  }

  it should " calculate full rotation " in {
    import scala.collection.immutable.Map
    val testData: Map[(Vector2D, Vector2D), Int] = Map(

      ((None, Some(UP)), (Some(LEFT), Some(UP))) -> -45,
      ((Some(LEFT), Some(UP)), (None, Some(UP))) -> 45,
      ((None, Some(UP)), (Some(LEFT), None)) -> -90,
      ((Some(LEFT), None), (None, Some(UP))) -> 90,

      ((None, Some(UP)), (Some(RIGHT), Some(UP))) -> -315,
      ((Some(RIGHT), Some(UP)), (None, Some(UP))) -> 315,
      ((None, Some(UP)), (Some(RIGHT), None)) -> -270,
      ((Some(RIGHT), None), (None, Some(UP))) -> 270,
      ((Some(LEFT), Some(UP)), (Some(RIGHT), Some(DOWN))) -> -180
    )

    for (d <- testData.keys) {
      val (f, t) = d
      Tank.getAngleFull(f, t).toInt should equal(testData(d))
    }
  }

  it should " inspect if it is in position" in {
    Tank.isInPosition(5, 5)(gTestContext) should equal((true, true))
    Tank.isInPosition(799, 5)(gTestContext) should equal((false, true))
    Tank.isInPosition(2, 5)(gTestContext) should equal((false, true))
    Tank.isInPosition(5, 1)(gTestContext) should equal((true, false))
    Tank.isInPosition(5, 799)(gTestContext) should equal((true, false))
  }

}
