package sk.kave.tank.map

import org.junit.{Assert, Test, Before}
import sk.kave.tank.beans.Tank
import sk.kave.tank.fx.{RIGHT, DOWN, LEFT, UP}

class TankTest {

  //def getAngle( from : Vector2D, to : Vector2D)

//      (None, Some(UP)), (Some(LEFT), Some(UP)),
//      (Some(LEFT), None), (Some(LEFT), Some(DOWN)),
//      (None, Some(DOWN)), (Some(RIGHT), Some(DOWN)),
//      (Some(RIGHT), None), (Some(RIGHT), Some(UP)))

  @Test
  def rotationTest() {
    Assert.assertEquals( -45, Tank.getAngle( (None, Some(UP)), (Some(LEFT), Some(UP)) ).toInt )
    Assert.assertEquals(  45, Tank.getAngle( (Some(LEFT), Some(UP)), (None, Some(UP)) ).toInt )
    Assert.assertEquals( -90, Tank.getAngle( (None, Some(UP)), (Some(LEFT), None) ).toInt )
    Assert.assertEquals(  90, Tank.getAngle( (Some(LEFT), None), (None, Some(UP)) ).toInt )

    Assert.assertEquals(  45, Tank.getAngle( (None, Some(UP)), (Some(RIGHT), Some(UP)) ).toInt )
    Assert.assertEquals( -45, Tank.getAngle( (Some(RIGHT), Some(UP)), (None, Some(UP)) ).toInt )
    Assert.assertEquals(  90, Tank.getAngle( (None, Some(UP)), (Some(RIGHT), None) ).toInt )
    Assert.assertEquals( -90, Tank.getAngle( (Some(RIGHT), None), (None, Some(UP)) ).toInt )
  }
}
