package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import events.{TankRotationEvent, TankMoveEvent, TankEvent, MapChangeEvent}
import fx._
import beans.{Tank, Game}
import scala._
import scalafx.scene.image.{Image, ImageView}
import sk.kave.tank.actors.TimelineMessage
import scalafx.Includes._
import scala.Some

object MapGroup extends Group {

  val config = implicitly[Config]
  import config._

  val mapView = new MapView[Rectangle](  initRec )

  val map = Game.map
  val tank = Game.tank

  val tankNode = new ImageView {
      image = new Image( GameStage.getClass.getResource( "/tank.png").toString )
      x = tank.x  * itemSize + MapGroup.layoutX.intValue()
      y = tank.y  * itemSize + MapGroup.layoutY.intValue()
      fitWidth = config.tankSize * config.itemSize
      fitHeight = config.tankSize * config.itemSize
    }


  def init() {
    children = mapView.init() :+ tankNode
    layoutX = -((mapView.col + mapView.BORDER_SIZE) * itemSize)  //todo: maybe move this transformations
    layoutY = -((mapView.row + mapView.BORDER_SIZE) * itemSize)  //      to another place .?

    map.addListener(this, eventOccured)
    tank.addListener(this, eventOccured)
  }

  def destroy(){
    map.removeListener( this)
    tank.removeListener( this)
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

  def eventOccured(event: MapChangeEvent) {
    mapView.updateRec(event.row, event.col, event.newValue)
  }

  def eventOccured(event :TankEvent) {
    event match {
      case e @ TankMoveEvent(_,_,_) => moveTank(e)
      case e @ TankRotationEvent(_, _) => rotateTank(e)

    }
  }

  private def rotateTank( e: TankRotationEvent) {
    Main.controlerActor ! TimelineMessage[Number](
      100 ms,
      List((tankNode.rotate , tankNode.rotate() + Tank.getAngle(e.oldVector, tank.vect))),
      e.callback
    )
  }

  private def moveTank( e : TankMoveEvent)  {

    def getDirection(direction : Vector2D) =
        (
          direction._1 match {
            case Some(LEFT) => +config.itemSize
            case Some(RIGHT) => -config.itemSize
            case None => 0
          }
        ,
          direction._2 match {
            case Some(UP) => +config.itemSize
            case Some(DOWN) => -config.itemSize
            case None => 0
          }
        )

    val (h,v) = tank.vect
    val (dH, dV) = getDirection( tank.vect)
    if ( canMove(tank.vect)) {
      Main.controlerActor ! TimelineMessage[Number](
        10 ms,
        List((translateX, translateX() + dH ),
             (translateY, translateY() + dV ),
             (tankNode.translateX, tankNode.translateX() - dH),
             (tankNode.translateY, tankNode.translateY() - dV)),
        () => {
          MapGroup.move(v)
          MapGroup.move(h)

          e.callback()
        }
      )
    } else
      e.callback()
  }

}
