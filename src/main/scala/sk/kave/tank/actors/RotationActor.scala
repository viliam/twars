package sk.kave.tank.actors

import scala.actors.{SchedulerAdapter, Actor}
import sk.kave.tank.Config
import sk.kave.tank._
import beans.{Game, Tank}
import fx._
import fx.map.{MapGroup, GameStage}
import scalafx.animation.{Timeline, KeyFrame}
import scalafx.Includes._
import javafx.event.{ActionEvent, EventHandler}

/**
 * actor performing moves of tank
 *
 * @author Vil
 */
class RotationActor extends JfxActor {
  self =>

  private def tankRotate = GameStage.tankNode.rotate
  val mapActor = (new MovementActor).start()
  val tank = Game.tank

  var newVect : Vector2D = tank.vect

  private var isTimelineAlive = false

  def act() {
    link(Main.controlerActor)
    react {
      case (horizontal: Option[Horizontal], vertical: Option[Vertical])  =>
        if (!isTimelineAlive) {     //fixme : don't ignore keyreleased
          newVect = (horizontal, vertical)
          if (newVect != tank.vect ) {
            rotate
          }

          mapActor ! newVect
        } else {
          Main.controlerActor ! Action.CONTINUE
        }

        act()
      case Action.CONTINUE =>        //when one key si released, actor needs to continue
        Main.controlerActor ! Action.CONTINUE
        act()
    }
    }

  def rotate {
    isTimelineAlive = true

    logg.debug( "Start rotation from = " + tank.vect + "   to = " + newVect )

    new Timeline() {
      onFinished = new EventHandler[ActionEvent] {
        def handle(e: ActionEvent) {
          tank.vect = newVect
          isTimelineAlive = false
        }
      }

      keyFrames = Seq(
        at(0 ms) {
          Set( tankRotate -> tankRotate() )
        },
        at(80 ms) {
          Set( tankRotate -> (tankRotate() + Tank.getAngle( tank.vect, newVect ) ) )
        }
      )
    }.play
  }

}
