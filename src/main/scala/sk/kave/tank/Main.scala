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

  stage = MapStage

  val controlerActor = (new GameControlerActor(MapStage.mapGroup)).start()

}
