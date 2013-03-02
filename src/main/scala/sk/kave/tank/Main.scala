package sk.kave.tank

import actors.GameControllerActor
import fx.map.GameStage
import scalafx.application.JFXApp
import akka.actor.{Props, ActorSystem}
import utils.{LoggerObj, Logger}

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:11 PM
 */
object Main extends JFXApp with Logger {
  stage = GameStage


  override def main(args: Array[String]) {
    LoggerObj.loggUser = if (!args.isEmpty) args(0) else "default"
    super.main(args)
  }

  val system = ActorSystem("ControllerSystem")
  val controlerActor = system.actorOf(Props[GameControllerActor], name = "controller")
}
