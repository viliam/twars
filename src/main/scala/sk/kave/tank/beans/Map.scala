package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._
import events.{ShootEvent, MapEvent,  EventListener}
import fx._
import utils.Logger
import java.io.{BufferedReader, File, FileReader}
import collection.mutable.ArrayBuffer

object Map extends Logger {

  val items = readMapFromFile("mapaGround.mapa")
  lazy val m: Map = new MapImpl( items)

  def apply() = m

  def bound : (Int, Int) = ( items.size, items(0).size )

  val (mapWidth, mapHeight) : (Int, Int) = Map.bound

  private def readMapFromFile( fileName : String ) : COLUMNS = {
    val fileReader: FileReader = new FileReader(new File( fileName))
    val buffReader: BufferedReader = new BufferedReader(fileReader)

    var s: String = null
    val li: ArrayBuffer[Array[Items]] = new ArrayBuffer[Array[Items]]
    while ({s = buffReader.readLine; s} != null) {
      li += (for (a <- s) yield Items(a)).toArray
    }

    li.toArray
  }
}

trait Map extends EventListener[MapEvent] {

  def apply(c: Int, r: Int): Items

  def update(c: Int, r: Int,  newValue: Items)

  /*
  For given position and bounds return if is posible move to specific direction
   */
  def canMove(position : => (Int, Int),
              bounds   : => (Int, Int),
              direction: => Vector2D): Boolean

  def shoot(e : ShootEvent)
}
