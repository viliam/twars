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

package sk.kave.tank

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

  lazy val system = ActorSystem("TankActorSystem")

  stage = GameStage


  override def main(args: Array[String]) {
    LoggerObj.loggUser = if (!args.isEmpty) args(0) else "default"
    super.main(args)
  }

  def exit() {
    system.shutdown()
  }

}
