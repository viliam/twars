package sk.kave.tank.beans

import sk.kave.tank._
import events.{TankEvent, TankRotationEvent, EventListener, TankMoveEvent}
import sk.kave.tank.fx._
import scala.Some

object Tank {

  val transformation = Vector[Vector2D](//clockwise path
    (None, Some(UP)), (Some(LEFT), Some(UP)),
    (Some(LEFT), None), (Some(LEFT), Some(DOWN)),
    (None, Some(DOWN)), (Some(RIGHT), Some(DOWN)),
    (Some(RIGHT), None), (Some(RIGHT), Some(UP)))

  def getAngle(from: Vector2D, to: Vector2D): Double = {
    val i1 = transformation.indexOf(from)
    val i2 = transformation.indexOf(to)

    val n = (i1 - i2)

    //sometimes clockwise direction isn't shortest path
    if (math.abs(n) > transformation.size / 2) {
      (transformation.size - math.abs(n)) * math.signum(n) * -45
    }
    else {
      n * 45
    }
  }

  def isInPosition(x: Int, y: Int)(implicit gContext: Game): (Boolean, Boolean) = {
    import gContext.config._

    val h = if ((x > Map().maxCols - width / 2)  || (x < width / 2)) false else true
    val v = if ((y > Map().maxRows - height / 2) || (y < height / 2)) false else true

    (h, v)
  }
}

class Tank(
            @volatile private var _x: Int,
            @volatile private var _y: Int,
            @volatile private var _vect: Vector2D = (None, Some(UP)))
          (implicit gContext : Game) extends EventListener[TankEvent] {


  import gContext._
  import gContext.config._

  def x = _x

  def x_=(v: Int) {
    _x = v
  }

  def y = _y

  def y_=(v: Int) {
    _y = v
  }

  def vect = _vect

  def vect_=(v: Vector2D) {
    _vect = v
  }

  def changeDirection(vect: Vector2D)(callBack: () => Unit) {
    val oldVect = this.vect
    this.vect = vect

    fireEvent(TankRotationEvent(oldVect, callBack))
  }

  def cleanGround(): Int = {
    var groundCount = 0
    for (c <- x until x + tankSize;
         r <- y until y + tankSize) {
      if (map(c, r) == Ground) groundCount = groundCount + 1
      map(c, r) = Grass
    }
    groundCount
  }

  def canMove(vect: Vector2D) = map.canMove((x, y), (tankSize, tankSize), vect) && !isStoneAhead(vect)

  private def isStoneAhead(direction: Vector2D): Boolean = {

    val x = direction.horizontal match {
      case Some(RIGHT) => _x + 1
      case Some(LEFT) => _x - 1
      case None => _x
    }

    val y = direction.vertical match {
      case Some(DOWN) => _y + 1
      case Some(UP) => _y - 1
      case None => _y
    }

    for (c <- x until x + tankSize;
         r <- y until y + tankSize) {
      if (map(c, r) == Stone) return true
    }

    false
  }

  def move(vect: Vector2D)(callback: () => Unit) {
    if (!canMove(vect)) {
      debug("tank cannot move anymore", All)
      callback()
      return
    }

    vect.horizontal match {
      case Some(RIGHT) => _x = x + 1
      case Some(LEFT) => _x = x - 1
      case None =>
    }

    vect.vertical match {
      case Some(DOWN) => _y = y + 1
      case Some(UP) => _y = y - 1
      case None =>
    }

    val groundCleaned = cleanGround()
    debug("Tank cleared ground " + groundCleaned, Igor)

    val cb = () => {
      callback()
    }

    fireEvent(TankMoveEvent(this.x, this.y, vect, cb))
  }
}
