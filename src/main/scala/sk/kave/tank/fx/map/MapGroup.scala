package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import events.{TankRotationEvent, TankMoveEvent, TankEvent, MapChangeEvent}
import fx._
import beans.{Tank, Game}
import scala._
import scalafx.scene.image.{Image, ImageView}
import actors.TimelineMessage
import scalafx.Includes._
import scala.Some
import utils.Logger

object MapGroup extends Group with Logger {

  val config = implicitly[Config]

  import config._

  val mapView = new MapView[Rectangle](initRec)

  val map = Game.map
  val tank = Game.tank

  val tankNode = new ImageView {
    image = new Image(GameStage.getClass.getResource("/tank.png").toString)
    x = tank.x * itemSize + MapGroup.layoutX.intValue()
    y = tank.y * itemSize + MapGroup.layoutY.intValue()
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
      case e@TankMoveEvent(_, _, _, _) => handleMovement(e)
      case e@TankRotationEvent(_, _) => rotateTank(e)
    }
  }

  private def rotateTank(e: TankRotationEvent) {
    Main.controlerActor ! TimelineMessage[Number](
      100 ms,
      List((tankNode.rotate, tankNode.rotate() + Tank.getAngle(e.oldVector, tank.vect))),
      e.callback
    )
  }

  private def handleMovement(e: TankMoveEvent) {
    val (posH, posV) = Tank.isInPosition(e.x, e.y)
    if (canMapMove(e.direction) && ((posH, posV).equals(true, true))) {
      moveMap(e)
    } else {
      debug("posH = " + posH + " posV = " + posV, Igor)
      movementNearTheEdge(posH, posV, e)
    }
  }

  private def movementNearTheEdge(posH: Boolean, posV: Boolean, e: TankMoveEvent) {
    debug("movementNearTheEdge", Igor)
    val (dirH, dirV) = e.direction

    val evH = TankMoveEvent(e.x, e.y, (dirH, None): Vector2D, () => e.callback())
    val evV = TankMoveEvent(e.x, e.y, (None, dirV): Vector2D, () => e.callback())

    if (dirH.isDefined && dirV.isDefined) {
      moveTankAndMap(e)
      return
    }



    //horizontal movement
    if (dirH.isDefined) {
      if (posH) {
        //move the map if possible
        if (canMapMove(dirH, None)) {
          moveMap(evH)
        } else {
          e.callback()
        }
      } else {
        //move the tank if possible
        if (canTankMove(dirH, None)) {
          moveTank(evH, dirH, dirV)
        } else {
          e.callback()
        }
      }
    }
    //fixme refactor into one method with horizontal movement (if possible)
    //vertical movement
    if (dirV.isDefined) {
      if (posV) {
        //move the map if possible
        if (canMapMove(None, dirV)) {
          moveMap(evV)
        } else {
          e.callback()
        }
      } else {
        //move the tank if possible
        if (canTankMove(None, dirV)) {
          moveTank(evV, dirH, dirV)
        } else {
          e.callback()
        }
      }
    }
  }

  private def moveTankAndMap(e: TankMoveEvent) {
    def getDirection(direction: Vector2D) =
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


    val (h, v) = e.direction
    val (dH, dV) = getDirection(e.direction)
    Main.controlerActor ! TimelineMessage[Number](
      10 ms,
      List(
        if (h.isDefined) (translateX, translateX() + dH) else (translateY, translateY() + dV),
        (tankNode.translateX, tankNode.translateX() - dH),
        (tankNode.translateY, tankNode.translateY() - dV)),
      () => {
        if (h.isDefined) MapGroup.moveMap(h) else MapGroup.moveMap(v)

        e.callback()
      }
    )
  }

  private def moveTank(e: TankMoveEvent, dirH: Option[Horizontal], dirV: Option[Vertical]) {
    debug("move tank " + e, Igor)
    def getDirection(direction: Vector2D) =
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

    val (dH, dV) = getDirection(e.direction)

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

    def getDirection(direction: Vector2D) =
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

    val (h, v) = e.direction
    val (dH, dV) = getDirection(e.direction)
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
