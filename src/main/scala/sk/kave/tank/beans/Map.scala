package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._
import events.{MapChangeEvent, EventListener}
import fx._
import scala.Some
import utils.Logger

private[beans] object Map extends Logger {

  val items = readMapFromFile("mapaGround.mapa")
  lazy val m: Map = new MapImpl( items)

  def apply() = m

  def bound : (Int, Int) = ( items.size, items(0).size )
}

trait Map extends EventListener[MapChangeEvent] {

  def apply(c: Int, r: Int): Items

  def update(c: Int, r: Int,  newValue: Items)

  /*
  For given position and bounds return if is posible move to specific direction
   */
  def canMove(position : => (Int, Int),
              bounds   : => (Int, Int),
              direction: => Vector2D): Boolean

}
