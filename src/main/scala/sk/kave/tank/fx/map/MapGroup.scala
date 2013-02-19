package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import fx.{Vertical, Horizontal, Direction}
import sk.kave.tank.beans.Map
import scala.collection.mutable.ListBuffer
import scalafx.animation.{KeyFrame, Timeline}
import javafx.event.{ActionEvent, EventHandler}
import scala._
import scala.collection.mutable

import scala.Some

object MapGroup extends Group {

  val map = Map()
  val mapView = new MapView

  def init() {
    children = mapView.init()
  }


  def move(dir: Option[Direction]) {
    mapView.move(dir)
  }

  def canMove(tuple: (Option[Horizontal], Option[Vertical])): Boolean = {
    mapView.canMove(tuple)
  }

  /**
   * inits x,y and fill color
   */
  private[fx] def initRec(rec: Rectangle, iCol: Int, iRow: Int) = {
    rec.x = iCol * ItemSize
    rec.y = iRow * ItemSize

    rec.fill = map(iCol, iRow).fillColor

    rec
  }
}
