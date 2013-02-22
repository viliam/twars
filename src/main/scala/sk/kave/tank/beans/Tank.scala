package sk.kave.tank.beans

import sk.kave.tank._
import sk.kave.tank.fx._
import scala.Some
import scala.Some
import sk.kave.tank.map
import scala.Some

object Tank {

  val transformation = Vector[Vector2D](     //clockwise path
    (None, Some(UP)), (Some(LEFT), Some(UP)),
    (Some(LEFT), None), (Some(LEFT), Some(DOWN)),
    (None, Some(DOWN)), (Some(RIGHT), Some(DOWN)),
    (Some(RIGHT), None), (Some(RIGHT), Some(UP)))

  def getAngle( from : Vector2D, to : Vector2D) : Int = {
    val i1 = transformation.indexOf(from)
    val i2 = transformation.indexOf(to)

    (i1 - i2) * 45
  }
}

class Tank (
    @volatile var x: Int,
    @volatile var y: Int,
    @volatile var vect : Vector2D = ( None, Some(UP)) ) (implicit config: Config) {

  import config._

  val map = Game.map

  for ( c <- x until x+tankSize;
        r <- y until y+tankSize) map.update(c, r, NoMap)

  def canMove(vect : Vector2D) = map.canMove( (x,y), (tankSize,tankSize), vect)

  def move( vect: Option[Direction]) {
    vect match {
      case Some(DOWN)  => y = y +1
      case Some(UP)    => y = y -1
      case Some(RIGHT) => x = x +1
      case Some(LEFT)  => x = x -1
      case None =>
    }
  }
}
