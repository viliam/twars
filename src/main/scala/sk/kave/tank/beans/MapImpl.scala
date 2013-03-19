package sk.kave.tank.beans

import sk.kave.tank._
import events.{ShootEvent, MapChangeEvent}
import fx.{DOWN, UP, RIGHT, LEFT}
import scala.Some

private[beans] class MapImpl(val items: COLUMNS)(implicit gContext : Game) extends Map {

  val maxCols: Int = items.size
  val maxRows: Int = items(0).size

  override def apply(c: Int, r: Int): Items = {
    if (r >= maxRows || r < 0 || c >= maxCols || c < 0) {
      return NoMap
    }
    items(c)(r)
  }

  override def update(c: Int, r: Int,  newValue: Items) {
    items(c)(r) = newValue
    fireEvent( MapChangeEvent( c, r, newValue))
  }


  /*
  For given position and bounds return if is posible move to specific direction
   */
  override def canMove(position : => (Int, Int),
              bounds   : => (Int, Int),
              direction: => Vector2D): Boolean = {
      val (col,row) = position
      val (width,height) = bounds
      val (horizontal, vertical) = direction
      val result =
        (horizontal match {
          case Some(LEFT)  if (col <= 0) =>   false
          case Some(RIGHT) if (col >= maxCols - width) =>  false
          case _ => true
        })&&
          (vertical match {
            case Some(UP)   if (row <= 0) => false
            case Some(DOWN) if (row >= maxRows - height) => false
            case _ => true
          })
      if (!result) {
        debug("cannot move",All)
      }

      result
    }

  override def shoot(e : ShootEvent) {
    //todo, check map, maybe clean map or some tank is shooted
    val cb = () => {
      val (xx,yy) = e.bullet.direction.getShift(e.x,e.y)
      gContext.map.shoot( ShootEvent(xx, yy, e.bullet, e.callback ) )
      e.callback()
    }

    fireEvent(ShootEvent(e.x, e.y, e.bullet, cb))
  }
}
