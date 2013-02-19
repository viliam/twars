package sk.kave

import tank.beans.{Items, Map}
import java.io.{BufferedReader, File, FileReader}
import collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scalafx.beans.property.DoubleProperty

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 2:17 PM
 */
package object tank {

  trait Config {
      def width  : Int
      def height : Int
      def itemSize : Int
  }

  implicit object ConfigImpl extends Config {
    val width = 900
    val height = 900

    val itemSize = 10

  }


  val logg = Logger.getLogger(Main.getClass)

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
