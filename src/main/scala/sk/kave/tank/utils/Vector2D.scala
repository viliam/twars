package sk.kave.tank.utils

import sk.kave.tank._
import fx._
import scala.Some
import scala.Some

/**
 * @author Igo
 */
case class Vector2D(value: (Option[Horizontal], Option[Vertical]))(implicit config:Config) {
  def apply(): (Option[Horizontal], Option[Vertical]) = value

  /**
   * calculates shift of a object according to Vector2D value
   * @return
   */
  def getShift =
       (
         value._1 match {
           case Some(LEFT) => +config.itemSize
           case Some(RIGHT) => -config.itemSize
           case None => 0
         }
         ,
         value._2 match {
           case Some(UP) => +config.itemSize
           case Some(DOWN) => -config.itemSize
           case None => 0
         }
         )
}
