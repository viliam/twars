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

  val mapGroup = MapGroup
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
