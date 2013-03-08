package sk.kave.tank.utils

import sk.kave.tank._
import fx._
import scala.Some

/**
 * @author Igo
 */
case class Vector2D(value: (Option[Horizontal], Option[Vertical])) {
  val horizontal = value._1
  val vertical = value._2

  def apply(): (Option[Horizontal], Option[Vertical]) = value

  /**
   * calculates shift of a object according to Vector2D value
   * @return
   */
  def getShift(px : Int) =
    (
      value._1 match {
        case Some(LEFT)  => +px
        case Some(RIGHT) => -px
        case None => 0
      }
      ,
      value._2 match {
        case Some(UP)   => +px
        case Some(DOWN) => -px
        case None => 0
      }
      )

  /**
   * at least one direction is defined
   * @return
   */
  def isDefined: Boolean = value._1.isDefined || value._2.isDefined

}

