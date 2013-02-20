package sk.kave.tank.fx

import actors.Actor
import map.{GameStage, MapGroup}
import sk.kave.tank._
import sk.kave.tank.beans.Tank
import scalafx.animation.Timeline
import javafx.event.{ActionEvent, EventHandler}
import scalafx.Includes._
import scala.Some
import java.util.concurrent.CountDownLatch

/**
 * actor performing moves
 *
 * @author Igo & Vil
 */
class MovingActor(val gameControllerActor: GameControllerActor) extends Actor {
  self =>

  val config = implicitly[Config]

  var vect : Vector2D = ( None, Some(UP))  //todo move to tank

  def act() {
    react {
      case (Action.EXIT, _) =>
        logg.info("moving actor says 'Good bye'")
      case (horizontal: Option[Horizontal], vertical: Option[Vertical]) =>
        move(horizontal, vertical)
        act()
    }
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

  private def translateX = MapGroup.translateX
  private def translateY = MapGroup.translateY

  private def tankRotate = GameStage.tankNode.rotate

  private def move(vec : Vector2D) {
    val (horizontal, vertical) = vec
    if (!MapGroup.canMove((horizontal, vertical))) {
      gameControllerActor ! Action.CONTINUE
      return
    }


    runInJFXthread {
      val v = vertical
      val h = horizontal

      new Timeline() {
        onFinished = new EventHandler[ActionEvent] {
          def handle(e: ActionEvent) {
            MapGroup.move(v)
            MapGroup.move(h)

            vect = (h, v)

            gameControllerActor ! Action.CONTINUE
          }
        }

        keyFrames = Seq(
          at(0 ms) {
            Set(translateX -> translateX(),
                translateY -> translateY(),
                tankRotate -> tankRotate()
            )
          },
          at(10 ms) {
            Set(translateX -> (translateX() + getDirectionHorizontal(h)),
                translateY -> (translateY() + getDirectionVertical(v)),
                tankRotate -> (tankRotate() + Tank.getAngle( vect, (h,v) ) ) )
          }
        )
      }.play
    }
  }

  private def runInJFXthread(runThis: => Unit) {
    //val latch = new CountDownLatch(1)
    javafx.application.Platform.runLater(new Runnable() {
      def run() {
        runThis
        //latch.countDown()
      }
    })
    //latch.await()
  }
}
