package sk.kave.tank.maps

import org.junit.{Assert, Test, Before}
import sk.kave.tank.beans.{TankImpl, Tank}
import sk.kave.tank.fx.{RIGHT, DOWN, LEFT, UP}
import sk.kave.tank.Config
import javafx.util.Duration

class TankTest {

  implicit var testConfig: Config = null

  @Before
  def before() {
    testConfig = new Config() {
      def width = 10

      def height = 10

      def itemSize = 1

      def tankSize = 2

      def tankRotationDuration:Duration = 10 ms

      def tankMovementDuration:Duration = 10 ms
    }
  }

  @Test
  def testRotation() {
    Assert.assertEquals(-45, Tank.getAngle((None, Some(UP)), (Some(LEFT), Some(UP))).toInt)
    Assert.assertEquals(45, Tank.getAngle((Some(LEFT), Some(UP)), (None, Some(UP))).toInt)
    Assert.assertEquals(-90, Tank.getAngle((None, Some(UP)), (Some(LEFT), None)).toInt)
    Assert.assertEquals(90, Tank.getAngle((Some(LEFT), None), (None, Some(UP))).toInt)

    Assert.assertEquals(45, Tank.getAngle((None, Some(UP)), (Some(RIGHT), Some(UP))).toInt)
    Assert.assertEquals(-45, Tank.getAngle((Some(RIGHT), Some(UP)), (None, Some(UP))).toInt)
    Assert.assertEquals(90, Tank.getAngle((None, Some(UP)), (Some(RIGHT), None)).toInt)
    Assert.assertEquals(-90, Tank.getAngle((Some(RIGHT), None), (None, Some(UP))).toInt)
  }

  @Test
  def testIsInPosition() {
    Assert.assertEquals((true, true), Tank.isInPosition(5, 5))

    Assert.assertEquals((false, true), Tank.isInPosition(799, 5))
    Assert.assertEquals((false, true), Tank.isInPosition(2, 5))
    Assert.assertEquals((true, false), Tank.isInPosition(5, 1))
    Assert.assertEquals((true, false), Tank.isInPosition(5, 799))
  }
}
