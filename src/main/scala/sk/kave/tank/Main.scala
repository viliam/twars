package sk.kave.tank

import fx.map.MapStage
import fx.{Action, GameControllerActor}
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

  val controlerActor = (new GameControllerActor(MapStage.mapGroup)).start()

}
