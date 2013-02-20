package sk.kave.tank.beans

import sk.kave.tank.fx.{UP, LEFT, DOWN, RIGHT}
import sk.kave.tank._

object Tank {  //just for prototype. TODO: solve reference and context model

  val transformation = Vector[Vector2D](     //clockwise path
    (None, Some(UP)), (Some(LEFT), Some(UP)),
    (Some(LEFT), None), (Some(LEFT), Some(DOWN)),
    (None, Some(DOWN)), (Some(RIGHT), Some(DOWN)),
    (Some(RIGHT), None), (Some(RIGHT), Some(UP)))

  def getAngle( from : Vector2D, to : Vector2D) : Int = {
    val i1 = transformation.indexOf(from)
    val i2 = transformation.indexOf(to)

//    if (i2 == -1)
//      0
//    else
      (i1 - i2) * 45
  }
}
