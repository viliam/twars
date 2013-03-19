package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import events._
import events.MapChangeEvent
import events.TankMoveEvent
import events.TankRotationEvent
import fx._
import sk.kave.tank.beans.{Bullet, Tank, Game}
import scala._
import scalafx.scene.image.{Image, ImageView}
import actors.{TimelineActor, ContinueMovement, TimelineMessage}
import utils.Logger
import akka.actor.Props
import scala.Some
import scalafx.application.Platform

object MapGroup extends Group with Logger {

  val gContext = implicitly[Game]
  import gContext._
  import gContext.config._

  private lazy val timelineActor = Main.system.actorOf(Props[TimelineActor])

  val mapView = new MapView[Rectangle](initRec)

  val tankNode = new ImageView {
    image = new Image(GameStage.getClass.getResource("/tank.png").toString)
    x = initG.tankX * itemSize + MapGroup.layoutX.intValue()
    y = initG.tankY * itemSize + MapGroup.layoutY.intValue()
    fitWidth = config.tankSize * config.itemSize
    fitHeight = config.tankSize * config.itemSize
  }

  var bullets : Map[Bullet, ImageView] = Map()


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

  def eventOccured(event: MapEvent) {
    event match {
      case e: MapChangeEvent => mapView.updateRec(e.col, e.row, e.newValue)
      case e: ShootEvent => shoot(e)
    }
  }

  def eventOccured(event: TankEvent) {
    event match {
      case e : TankMoveEvent => handleMovement(e)
      case e : TankRotationEvent => rotateTank(e)
    }
  }

  private def rotateTank(e: TankRotationEvent) {
    timelineActor ! TimelineMessage[Number](
      tankRotationDuration,
      List((tankNode.rotate, tankNode.rotate() + Tank.getAngle(e.oldDirection , e.newDirection ))), ()=>{
        e.callback()
        if (tank.direction.isDefined){
          tankActor ! ContinueMovement
        }
      }
    )
  }

  private def shoot(e : ShootEvent) {
    debug("Shoot: " + e, Vilo)
    //ak strela neexistuje- vytvor strelu
    val bullet = getBullet(e)
    val (dH, dV) = e.bullet.direction.getShift( itemSize)
    timelineActor ! TimelineMessage[Number](
      bulletMovementDuration,
      List(
        (bullet.translateX, bullet.translateX() - dH),
        (bullet.translateY, bullet.translateY() - dV)),
      () => {
        e.callback()
      }
    )
  }

  private def getBullet(e : ShootEvent) : ImageView = {
    if (bullets.contains( e.bullet) ) bullets(e.bullet)
    else {
      val i = new ImageView {
        image = new Image(GameStage.getClass.getResource("/bullet.png").toString)
        x = e.x * itemSize
        y = e.y * itemSize
        fitWidth = config.itemSize
        fitHeight =config.itemSize
      }
      Platform.runLater{ this.children.add( i); ()}
      bullets = bullets + (e.bullet -> i)
      i
    }
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

    def movement(canMove: Boolean, inPosition: Boolean, event: TankMoveEvent, dirNew:Vector2D) {
      if (canMove) {
        if (inPosition) {
          //move the map if possible
          if (canMapMove(dirNew.horizontal, dirNew.vertical)) {
            moveMap(event)
          } else {
            e.callback()
          }
        } else {
          //move the tank if possible
          if (canTankMove(dirNew.horizontal, dirNew.vertical)) {
            moveTank(event, dirNew.horizontal, dirNew.vertical)
          } else {
            e.callback()
          }
        }
      }
    }

    val evH = TankMoveEvent(e.x, e.y, (dirH, None): Vector2D, () => e.callback())
    val evV = TankMoveEvent(e.x, e.y, (None, dirV): Vector2D, () => e.callback())

    //horizontal movement
    movement(dirH.isDefined, posH, evH, (dirH, None))
    //vertical movement
    movement(dirV.isDefined, posV, evV, (None, dirV))
  }

  private def moveTankAndMap(e: TankMoveEvent, posH: Boolean, posV: Boolean) {

    val (h, v) = e.direction
    val (dH, dV) = e.direction.getShift( itemSize)
    timelineActor ! TimelineMessage[Number](
      tankMovementDuration,
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
    timelineActor ! TimelineMessage[Number](
      tankMovementDuration,
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
      timelineActor ! TimelineMessage[Number](
        tankMovementDuration,
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
