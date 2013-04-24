/*
 * Copyright
 *  viliam.kois@gmail.com   Kois Viliam
 *  ikvaso@gmail.com        Kvasnicka Igor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package sk.kave.tank.fx.map

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._

import events._
import events.MapChangeEvent
import events.TankMoveEvent
import events.TankRotationEvent
import fx._
import sk.kave.tank.beans.{IGameContext, Bullet, Tank}
import scala._
import scalafx.scene.image.{Image, ImageView}
import sk.kave.tank.actors._
import utils.{Vector2D, Logger}
import akka.actor.Props
import scala.Some
import scalafx.application.Platform
import sk.kave.tank.actors.Shoot
import sk.kave.tank.events.BulletExplodedEvent
import sk.kave.tank.events.MapChangeEvent
import scala.Some
import sk.kave.tank.actors.TimelineMessage
import sk.kave.tank.events.TankMoveEvent
import sk.kave.tank.events.ShootEvent
import sk.kave.tank.utils.Vector2D
import sk.kave.tank.events.TankRotationEvent
import sk.kave.tank.actors.ContinueMovement

object MapGroup {}

class MapGroup(implicit gContext: IGameContext) extends Group with Logger {

  import gContext._
  import gContext.config._

  private lazy val timelineActor = Main.system.actorOf(Props[TimelineActor])

  val mapView = new MapView[Rectangle](initRec)(gContext)

  val tankNode = new ImageView {
    image = new Image(MapGroup.getClass.getResource("/tank.png").toString)
    x = initG.tankX * itemSize + layoutX.intValue()
    y = initG.tankY * itemSize + layoutY.intValue()
    fitWidth = config.tankSize * config.itemSize
    fitHeight = config.tankSize * config.itemSize
  }

  var bullets: Map[Bullet, ImageView] = Map()


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

  def canMapMove(tuple: Vector2D): Boolean = {
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
      case e: MapChangeEvent => mapView.updateRec(e.col, e.row)
      case e: ShootEvent => shoot(e)
      case e: BulletExplodedEvent => {
        hideBullet(e.bullet)
        //when bullet explodes too close to the tank, new bullet should be created (if player is still shooting)
        if (e.bullet.distanceFromSource<Bullet.BULLET_DIFFERENCE){
          tankActor ! CreateNewBullet(()=>())
        }
      }
    }
  }

  def eventOccured(event: TankEvent) {
    event match {
      case e: TankMoveEvent => handleMovement(e)
      case e: TankRotationEvent => rotateTank(e)
    }
  }

  private def rotateTank(e: TankRotationEvent) {
    timelineActor ! TimelineMessage[Number](
      tankRotationDuration,
      List((tankNode.rotate, tankNode.rotate() + Tank.getAngle(e.oldDirection, e.newDirection))), () => {
        e.callback()
        if (tank.direction.isDefined) {
          tankActor ! ContinueMovement
        }
      }
    )
  }

  private def shoot(e: ShootEvent) {
    debug("Shoot: " + e, Vilo, Igor)
    //ak strela neexistuje- vytvor strelu
    val bullet = getBullet(e)
    val (dH, dV) = e.bullet.direction.getShift(itemSize)
    timelineActor ! TimelineMessage[Number](
      bulletMovementDuration,
      List(
        (bullet.translateX, bullet.translateX() - dH),
        (bullet.translateY, bullet.translateY() - dV)),
      () => {
        e.callback()
        //ask if new bullet can be created
        if (tank.lastCreatedBullet.isDefined && tank.lastCreatedBullet.get.canCreateNewBullet){
          tankActor ! CreateNewBullet(()=>())
        }
      }
    )
  }

  private def hideBullet(b: Bullet) {
    val bulletImage = bullets(b)
    Platform.runLater {
      new Runnable {
        def run() {
          MapGroup.this.children.remove(bulletImage)
        }
      }

    }
    bullets -= b
  }

  private def getBullet(e: ShootEvent): ImageView = {
    if (bullets.contains(e.bullet)) {
      bullets(e.bullet)
    }
    else {

      val i = new ImageView {
        image = new Image(GameStage.getClass.getResource("/bullet.png").toString)
        x = e.x * itemSize
        y = e.y * itemSize
        fitWidth = config.itemSize
        fitHeight = config.itemSize
      }
      Platform.runLater {
        this.children.add(i);
        ()
      }
      bullets = bullets + (e.bullet -> i)
      i
    }
  }

  private def handleMovement(e: TankMoveEvent) {
    val (inPositionH, inPositionV) = Tank.isInPosition(e.x, e.y)
    if (canMapMove(e.direction) && ((inPositionH, inPositionV).equals(true, true))) {
      //it is possible to move map without tank involved and tank is in its position
      moveMap(e)
    } else {
      movementNearTheEdge(inPositionH, inPositionV, e)
    }
  }

  private def movementNearTheEdge(posH: Boolean, posV: Boolean, e: TankMoveEvent) {
    val (dirH, dirV) = e.direction

    //diagonal movement must be handeled separately
    if (dirH.isDefined && dirV.isDefined) {
      moveTankAndMap(e, posH, posV)
      return
    }

    //handle horizontal and vertical movement separately, this allows to move tank AND map together
    //horizontal movement
    if (dirH.isDefined) {
      val tankMoveEventH = TankMoveEvent(e.x, e.y, (dirH, None): Vector2D, () => e.callback())
      movement(posH, tankMoveEventH, (dirH, None))
    }

    //vertical movement
    if (dirV.isDefined) {
      val tankMoveEventV = TankMoveEvent(e.x, e.y, (None, dirV): Vector2D, () => e.callback())
      movement(posV, tankMoveEventV, (None, dirV))
    }

    //--------methods
    def movement(inPosition: Boolean, event: TankMoveEvent, dirNew: Vector2D) {
      if (inPosition) {
        //move the map if possible
        move(event, dirNew, canMapMove, moveMap)
      } else {
        //move the tank if possible
        move(event, dirNew, canTankMove, moveTank)
      }
    }

    def move(moveEvent: TankMoveEvent, dir: Vector2D, canMoveFunction: (Vector2D => Boolean), performMovementFunction: (TankMoveEvent) => Unit) {
      if (canMoveFunction(dir)) {
        performMovementFunction(moveEvent)
      } else {
        moveEvent.callback()
      }
    }
  }

  private def moveTankAndMap(e: TankMoveEvent, posH: Boolean, posV: Boolean) {

    val (h, v) = e.direction
    val (dH, dV) = e.direction.getShift(itemSize)
    timelineActor ! TimelineMessage[Number](
      tankMovementDuration,
      List(
        if (posH) (translateX, translateX() + dH) else (translateY, translateY() + dV),
        (tankNode.translateX, tankNode.translateX() - dH),
        (tankNode.translateY, tankNode.translateY() - dV)),
      () => {

        if (posH) {
          if (canMapMove(h, None)) {
            moveMap(h)
          }
        } else {
          if (canMapMove(None, v)) {
            moveMap(v)
          }
        }

        e.callback()
      }
    )
  }

  private def moveTank(e: TankMoveEvent) {
    val (dH, dV) = e.direction.getShift(itemSize)
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
    val (dH, dV) = e.direction.getShift(itemSize)
    if (canMapMove(e.direction)) {
      timelineActor ! TimelineMessage[Number](
        tankMovementDuration,
        List((translateX, translateX() + dH),
          (translateY, translateY() + dV),
          (tankNode.translateX, tankNode.translateX() - dH),
          (tankNode.translateY, tankNode.translateY() - dV)),
        () => {
          moveMap(v)
          moveMap(h)

          e.callback()
        }
      )
    }
  }
}
