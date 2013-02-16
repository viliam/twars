package sk.kave.tank

import fx.{Action, GameControlerActor, MapGroup, MapStage}
import scalafx.application.JFXApp
import scalafx.animation.{KeyFrame, Animation, Timeline}
import javafx.event.{ActionEvent, EventHandler}
import scalafx.Includes._
import scalafx.util.Duration

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:11 PM
 */
object Main extends JFXApp {

  val GAME_LOOP_DELAY = 50.ms

  stage = MapStage

  val controlerActor = (new GameControlerActor(MapStage.mapGroup)).start()


  //this is a main game loop that takes care of periodical updating of player's position
  val gameLoop = new Timeline() {
    keyFrames = Seq(

      KeyFrame(
        GAME_LOOP_DELAY,
        "GameLoop KeyFrame",
        controlerActor ! Action.GAME_LOOP_UPDATE
      )

    )
    cycleCount = Animation.INDEFINITE
  }.play
}
