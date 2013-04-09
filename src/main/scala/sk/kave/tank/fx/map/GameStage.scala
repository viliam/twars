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

import scalafx.Includes._
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.stage.WindowEvent

import sk.kave.tank._
import actors.{Shoot, ChangeMovement, KeyPressEvent}
import beans.GameContextImpl
import fx.{UP, DOWN, LEFT, RIGHT}
import javafx.scene.paint.Color
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:11 PM
 */
object GameStage extends PrimaryStage {

  val gContext = implicitly[GameContextImpl]
  import gContext._
  import gContext.config._

  title = "ScalaFX Tetris"

  val mapGroup = new MapGroup
  mapGroup.init()

  scene = new Scene(config.width * itemSize, config.height * itemSize) {
    fill = Color.BLACK
    content = List(
      mapGroup
    )

    onKeyPressed =
      (e: KeyEvent) => {
        e.code match {
          case (KeyCode.W) => tankActor ! ChangeMovement(UP, KeyPressEvent.PRESSED)
          case (KeyCode.A) => tankActor ! ChangeMovement(LEFT, KeyPressEvent.PRESSED)
          case (KeyCode.D) => tankActor ! ChangeMovement(RIGHT, KeyPressEvent.PRESSED)
          case (KeyCode.S) => tankActor ! ChangeMovement(DOWN, KeyPressEvent.PRESSED)
          case (KeyCode.SPACE) => tankActor ! Shoot( KeyPressEvent.PRESSED)
          case _ => ()
        }
      }

    onKeyReleased =
      (e: KeyEvent) => {
        e.code match {
          case (KeyCode.W) => tankActor !ChangeMovement( UP, KeyPressEvent.RELEASED)
          case (KeyCode.A) => tankActor !ChangeMovement( LEFT, KeyPressEvent.RELEASED)
          case (KeyCode.D) => tankActor !ChangeMovement( RIGHT, KeyPressEvent.RELEASED)
          case (KeyCode.S) => tankActor !ChangeMovement( DOWN, KeyPressEvent.RELEASED)
          case (KeyCode.SPACE) => tankActor ! Shoot( KeyPressEvent.RELEASED)
          case (KeyCode.ESCAPE) => Main.exit()
          case _ => ()
        }
      }
  }

  onHiding = (e: WindowEvent) =>  Main.exit
}
