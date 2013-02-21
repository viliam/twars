package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import events.mapchanged.MapChangedEvent
import fx.{Vertical, Horizontal, Direction}
import beans.{Game, Map}
import scala._

import scala.Some

object MapGroup extends Group {

  val config = implicitly[Config]
  import config._

  val mapView = new MapView[Rectangle](  initRec )
  val map = Game.map

  def init() {
    children = mapView.init()
    layoutX = -((mapView.col + mapView.BORDER_SIZE) * itemSize)  //todo: maybe move this transformations
    layoutY = -((mapView.row + mapView.BORDER_SIZE) * itemSize)  //      to another place .?
    map.addListener(this, eventOccured)
  }

  def destroy(){
    map.removeListener(this)
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
  private[fx] def initRec(opRec: Option[Rectangle], iCol: Int, iRow: Int) = {
    val rec = opRec match {
      case None => new Rectangle() {
        width = config.itemSize + 2
        height = config.itemSize + 2
      }
      case Some(r) => r
    }

    rec.x = iCol * config.itemSize
    rec.y = iRow * config.itemSize

    rec.fill = map(iCol, iRow).fillColor

    rec
  }

  def eventOccured(event: MapChangedEvent) {
    mapView.updateRec(event.row, event.col, event.newValue)
  }
}
