package sk.kave.tank.actors

import sk.kave.tank.fx.map.{ MapGroup}
import sk.kave.tank._
import fx._
import scalafx.animation.{Timeline, KeyFrame}
import scalafx.Includes._
import scala.Some
import scala.actors.{SchedulerAdapter, Actor}
import javafx.event.{ActionEvent, EventHandler}

/**
 * actor performing moves of map
 *
 * @author Igo & Vil
 */
class MovementActor extends JfxActor {
  self =>

  private def translateX = MapGroup.translateX
  private def translateY = MapGroup.translateY

  val config = implicitly[Config]

  protected var newVect : Vector2D  = (None, None)

  def act() {
    link(Main.controlerActor)
    react {
      case (horizontal: Option[Horizontal], vertical: Option[Vertical]) =>
        newVect = (horizontal, vertical)
        move
        act()
    }
  }


  private def move() {
      if (!MapGroup.canMove(newVect)) {
        sender ! Action.CONTINUE
        return
      }

      new Timeline() {
        onFinished = new EventHandler[ActionEvent] {
          def handle(e: ActionEvent) {
            val (h,v) = newVect
            MapGroup.move(v)
            MapGroup.move(h)

            sender ! Action.CONTINUE
          }
        }

        keyFrames = Seq(
            at(0 ms) {
              Set(translateX -> translateX(),
                  translateY -> translateY()
              )
            },
            at(10 ms) {
              Set(translateX -> (translateX() + getDirectionHorizontal(newVect._1)),
                  translateY -> (translateY() + getDirectionVertical(newVect._2)) )
            }
          )
      }.play
    }


  private def getDirectionHorizontal(horizontal: Option[Horizontal]) =
    horizontal match {
      case Some(LEFT) => +config.itemSize
      case Some(RIGHT) => -config.itemSize
      case None => 0
    }

  private def getDirectionVertical(vertical: Option[Vertical]) =
    vertical match {
      case Some(UP) => +config.itemSize
      case Some(DOWN) => -config.itemSize
      case None => 0
    }
}
