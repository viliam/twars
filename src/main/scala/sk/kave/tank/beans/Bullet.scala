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
