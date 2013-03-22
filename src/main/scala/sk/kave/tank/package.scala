package sk.kave

import tank.beans._
import java.io.{BufferedReader, File, FileReader}
import collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scalafx.beans.property.DoubleProperty
import tank.fx.{UP, Vertical, Horizontal}
import tank.utils.Vector2D
import javafx.util.Duration
import tank.utils.Vector2D

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 2:17 PM
 */
package object tank {

  type Vector2D = ( Option[Horizontal], Option[Vertical])

  trait Config extends scalafx.Includes{
      def width  : Int
      def height : Int
      def itemSize : Int
      def tankSize : Int
      def tankRotationDuration:Duration   = 0 ms
      def tankMovementDuration:Duration   = 0 ms
      def bulletMovementDuration:Duration = 0 ms
  }

  object ConfigImpl extends Config {
    val width = 30
    val height = 30
    val itemSize = 10
    val tankSize = 3
    override val tankRotationDuration = 130 ms
    override val tankMovementDuration = 20 ms
    override val bulletMovementDuration = 12 ms
  }

  implicit object GameContext extends GameContextImpl

  implicit def convertVector(v:Vector2D):sk.kave.tank.utils.Vector2D= Vector2D(v)
  implicit def convertVector(v:sk.kave.tank.utils.Vector2D):Vector2D= v()


}
