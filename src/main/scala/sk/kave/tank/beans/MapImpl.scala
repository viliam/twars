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

import sk.kave.tank._
import events.{ShootEvent, MapChangeEvent}
import fx.{DOWN, UP, RIGHT, LEFT}
import scala.Some
import akka.actor.TypedActor

private[beans] class MapImpl(val items: COLUMNS) extends Map {

  val maxCols: Int = items.size
  val maxRows: Int = items(0).size

  def bound : (Int, Int) = ( maxCols, maxRows)

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

  override def shoot(e : ShootEvent)(implicit gContext : GameContextImpl) {
    //todo, check map, maybe clean map or some tank is shooted
    val cb = () => {
      val (xx,yy) = e.bullet.direction.getShift(e.x,e.y)
      if (canBulletMove(xx,yy,e.bullet.direction))
        gContext.map.shoot( ShootEvent(xx, yy, e.bullet, e.callback ) )
      e.callback()
    }

    fireEvent(ShootEvent(e.x, e.y, e.bullet, cb))
  }

  /**
   * bullet CANNOT move, when there is end of map, stone or another tank at the position
   */
  private def canBulletMove(xD: Double, yD: Double, dir: Vector2D)(implicit gContext : GameContextImpl): Boolean = {
    for ((x, y) <- getNeighbourItems(xD, yD)) {
      //end of map
      if (!canMove((x, y), (1,1), dir)){
        debug("bullet stops - map end", Igor)
        return false
      }

      //stone
      if (gContext.map(x,y) == Stone){
        debug("bullet stops - stone ahead", Igor)
        return false
      }
      //another tank
      //todo bullet stops when there is another tank in the way
    }
    true
  }

  /**
   * takes x and y (both Double) and returns all Items' positions
   */
  private def getNeighbourItems(x: Double, y: Double): Array[(Int,Int)] = {
     val x_es = Array(math.floor(x).toInt, math.ceil(x).toInt)
     val y_es = Array(math.floor(y).toInt, math.ceil(y).toInt)

     for (i <- x_es; j <- y_es) yield {
       (i, j)
     }
   }
}
