package sk.kave

import tank.beans.{Game, Items, Map}
import java.io.{BufferedReader, File, FileReader}
import collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scalafx.beans.property.DoubleProperty
import tank.fx.{UP, Vertical, Horizontal}
import tank.utils.Vector2D

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 2:17 PM
 */
package object tank {

  type Vector2D = ( Option[Horizontal], Option[Vertical])

  trait Config {
      def width  : Int
      def height : Int
      def itemSize : Int
      def tankSize : Int
  }

  object ConfigImpl extends Config {
    val width = 30
    val height = 30
    val itemSize = 10

    val tankSize = 5
  }

  implicit object GameContext extends Game
  val map = GameContext.map

  implicit def convertVector(v:Vector2D):sk.kave.tank.utils.Vector2D= Vector2D(v)
  implicit def convertVector(v:sk.kave.tank.utils.Vector2D):Vector2D= v()


  def readMapFromFile( fileName : String ) : Map = {
    val fileReader: FileReader = new FileReader(new File( fileName))
    val buffReader: BufferedReader = new BufferedReader(fileReader)

    var s: String = null
    val li: ArrayBuffer[Array[Items]] = new ArrayBuffer[Array[Items]]
    while ({s = buffReader.readLine; s} != null) {
      li += (for (a <- s) yield Items(a)).toArray
    }

    new Map(li.toArray)
  }
}
