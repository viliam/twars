package sk.kave.tank.beans

import sk.kave.tank._
import events.{TankEvent, TankRotationEvent, EventListener, TankMoveEvent}
import sk.kave.tank.fx._
import scala.Some

object Tank {

  val transformation = Vector[Vector2D](     //clockwise path
    (None, Some(UP)), (Some(LEFT), Some(UP)),
    (Some(LEFT), None), (Some(LEFT), Some(DOWN)),
    (None, Some(DOWN)), (Some(RIGHT), Some(DOWN)),
    (Some(RIGHT), None), (Some(RIGHT), Some(UP)))

  def getAngle( from : Vector2D, to : Vector2D) : Double   = {
    val i1 = transformation.indexOf(from)
    val i2 = transformation.indexOf(to)

    val n = (i1 - i2)

    //sometimes clockwise direction isn't shortest path
    if ( math.abs(n) > transformation.size/2) {
      (transformation.size - math.abs(n)) * (n/math.abs(n)) * -45
    }
    else n* 45
  }
}

class Tank (
    @volatile private var _x: Int,
    @volatile private var _y: Int,
    @volatile private var _vect : Vector2D = ( None, Some(UP)) )
           (implicit config: Config) extends EventListener[TankEvent] {

  import config._

  def x = _x
  def y = _y
  def vect = _vect

  def changeDirection(vect : Vector2D)( callBack : () => Unit)  {
    val oldVect = _vect
    _vect = vect
    fireEvent( TankRotationEvent( oldVect, callBack))
  }

  val map = Game.map

  for ( c <- x until x+tankSize;
        r <- y until y+tankSize) map.update(c, r, NoMap)

  def canMove(vect : Vector2D) = map.canMove( (x,y), (tankSize,tankSize), vect)

  def move( vect: Vector2D)(callback : () => Unit) {  //todo fire event
    val (h,v) = vect

    h match {
      case Some(RIGHT) => _x = x +1
      case Some(LEFT)  => _x = x -1
      case None =>
    }

    v match {
      case Some(DOWN)  => _y = y +1
      case Some(UP)    => _y = y -1
      case None =>
    }
    fireEvent(TankMoveEvent( _x, _y, callback))
  }
}
