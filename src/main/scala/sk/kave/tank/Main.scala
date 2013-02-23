package sk.kave.tank

import actors.GameControllerActor
import fx.map.GameStage
import scalafx.application.JFXApp
import akka.actor.{Props, ActorSystem, ActorDSL}

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:11 PM
 */
object Main extends JFXApp {
  stage = GameStage

  val system = ActorSystem("ControllerSystem")
  val controlerActor = system.actorOf(Props[GameControllerActor], name = "controller")

}
