package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import events.{TankRotationEvent, TankMoveEvent, TankEvent, MapChangeEvent}
import fx._
import beans.{Tank, Game}
import scala._
import scalafx.scene.image.{Image, ImageView}
import actors.{ContinueMovement, TimelineMessage}
import scalafx.Includes._
import scala.Some
import utils.Logger

object MapGroup extends Group with Logger {

  val gContext = implicitly[Game]
  import gContext._
  import gContext.config._

  val mapView = new MapView[Rectangle](initRec)

  val tankNode = new ImageView {
    image = new Image(GameStage.getClass.getResource("/tank.png").toString)
    x = initG.tankX * itemSize + MapGroup.layoutX.intValue()
    y = initG.tankY * itemSize + MapGroup.layoutY.intValue()
    fitWidth = config.tankSize * config.itemSize
    fitHeight = config.tankSize * config.itemSize
  }


  def init() {
    children = mapView.init() :+ tankNode
    layoutX = -((mapView.col + mapView.BORDER_SIZE) * itemSize) //todo: maybe move this transformations
    layoutY = -((mapView.row + mapView.BORDER_SIZE) * itemSize) //      to another place .?

    map.addListener(this, eventOccured)
    tank.addListener(this, eventOccured)
  }

  def destroy() {
    map.removeListener(this)
    tank.removeListener(this)
  }


  def moveMap(dir: Option[Direction]) {
    mapView.move(dir)
  }

  def canMapMove(tuple: (Option[Horizontal], Option[Vertical])): Boolean = {
    mapView.canMove(tuple)
  }

  private def canTankMove(tuple: Vector2D): Boolean = {
    tank.canMove(tuple)
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
    mapView.updateRec(event.col, event.row, event.newValue)
  }

  def eventOccured(event: TankEvent) {
    event match {
      case e : TankMoveEvent => handleMovement(e)
      case e : TankRotationEvent => rotateTank(e)
    }
  }

  private def rotateTank(e: TankRotationEvent) {
    Main.controlerActor ! TimelineMessage[Number](
    10 ms,
    List((tankNode.rotate, tankNode.rotate() + Tank.getAngle(e.oldDirection , e.newDirection ))), ()=>{
      e.callback()
      if (tank.direction.isDefined){
        Main.controlerActor ! ContinueMovement(tank.direction)
      }
    }
    )
  }

  private def handleMovement(e: TankMoveEvent) {
    val (posH, posV) = Tank.isInPosition(e.x, e.y)
    if (canMapMove(e.direction) && ((posH, posV).equals(true, true))) {
      moveMap(e)
    } else {
      movementNearTheEdge(posH, posV, e)
    }
  }

  private def movementNearTheEdge(posH: Boolean, posV: Boolean, e: TankMoveEvent) {
    val (dirH, dirV) = e.direction

    //diagonal movement must be handeled separately
    if (dirH.isDefined && dirV.isDefined) {
      moveTankAndMap(e, posH, posV)
      return
    }



    def movement(dirDefined: Boolean, pos: Boolean, event: TankMoveEvent, dirH_temp: Option[Horizontal], dirV_temp: Option[Vertical]) {
      if (dirDefined) {
        if (pos) {
          //move the map if possible
          if (canMapMove(dirH_temp, dirV_temp)) {
            moveMap(event)
          } else {
            e.callback()
          }
        } else {
          //move the tank if possible
          if (canTankMove(dirH_temp, dirV_temp)) {
            moveTank(event, dirH, dirV)
          } else {
            e.callback()
          }
        }
      }
    }

    val evH = TankMoveEvent(e.x, e.y, (dirH, None): Vector2D, () => e.callback())
    val evV = TankMoveEvent(e.x, e.y, (None, dirV): Vector2D, () => e.callback())

    //horizontal movement
    movement(dirH.isDefined, posH, evH, dirH, None)
    //vertical movement
    movement(dirV.isDefined, posV, evV, None, dirV)
  }

  private def moveTankAndMap(e: TankMoveEvent, posH: Boolean, posV: Boolean) {

    val (h, v) = e.direction
    val (dH, dV) = e.direction.getShift( itemSize)
    Main.controlerActor ! TimelineMessage[Number](
      10 ms,
      List(
        if (posH) (translateX, translateX() + dH) else (translateY, translateY() + dV),
        (tankNode.translateX, tankNode.translateX() - dH),
        (tankNode.translateY, tankNode.translateY() - dV)),
      () => {

        if (posH) {
          if (canMapMove(h, None)) {
            MapGroup.moveMap(h)
          }
        } else {
          if (canMapMove(None, v)) {
            MapGroup.moveMap(v)
          }
        }

        e.callback()
      }
    )
  }

  private def moveTank(e: TankMoveEvent, dirH: Option[Horizontal], dirV: Option[Vertical]) {
    val (dH, dV) = e.direction.getShift( itemSize)
    Main.controlerActor ! TimelineMessage[Number](
      10 ms,
      List(
        (tankNode.translateX, tankNode.translateX() - dH),
        (tankNode.translateY, tankNode.translateY() - dV)),
      () => {
        e.callback()
      }
    )
  }


  private def moveMap(e: TankMoveEvent) {
    val (h, v) = e.direction
    val (dH, dV) = e.direction.getShift( itemSize)
    if (canMapMove(e.direction)) {
      Main.controlerActor ! TimelineMessage[Number](
        10 ms,
        List((translateX, translateX() + dH),
          (translateY, translateY() + dV),
          (tankNode.translateX, tankNode.translateX() - dH),
          (tankNode.translateY, tankNode.translateY() - dV)),
        () => {
          MapGroup.moveMap(v)
          MapGroup.moveMap(h)

          e.callback()
        }
      )
    }
  }
}
