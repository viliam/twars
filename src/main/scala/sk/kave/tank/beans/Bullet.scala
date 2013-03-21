package sk.kave.tank.beans

import sk.kave.tank.fx
import fx.RIGHT
import sk.kave.tank.utils.Vector2D

/**
 * User: vilo
 */
object Bullet{

  def getInitPosition(bulletDir:Vector2D, tankCenter:(Int,Int), tankSize:Int):(Double,Double)={
    val bulletShift = calculateBulletShift(Tank.getAngleFull((Some(RIGHT), None),bulletDir),tankSize)

    (tankCenter._1 + bulletShift._1/2,tankCenter._2 + bulletShift._2/2)
  }

  /**
    * calculates bullet shift according to tank rotation
    * this will create an effect of shooting from the tank's barrel
    */
   private def calculateBulletShift(tankAngle:Double, tankSize:Int):(Double,Double)={
     val x = tankSize/2 * math.cos(math.toRadians(tankAngle))

     val y = tankSize/2 * math.sin(math.toRadians(tankAngle))
     (math.signum(math.round(x)), math.signum(math.round(y)))
   }
}

class Bullet(val direction: Vector2D) {

}
