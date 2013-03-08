package sk.kave.tank.fx.map

import scalafx.Includes._
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.stage.WindowEvent

import sk.kave.tank._
import actors.{UserMessage, KeyPressEvent, Exit}
import beans.Game
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

  val gContext = implicitly[Game]
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
          case (KeyCode.W) => Main.controlerActor ! UserMessage(UP, KeyPressEvent.PRESSED)
          case (KeyCode.A) => Main.controlerActor ! UserMessage(LEFT, KeyPressEvent.PRESSED)
          case (KeyCode.D) => Main.controlerActor ! UserMessage(RIGHT, KeyPressEvent.PRESSED)
          case (KeyCode.S) => Main.controlerActor ! UserMessage(DOWN, KeyPressEvent.PRESSED)
          case _ => ()
        }
      }

    onKeyReleased =
      (e: KeyEvent) => {
        e.code match {
          case (KeyCode.W) => Main.controlerActor !UserMessage( UP, KeyPressEvent.RELEASED)
          case (KeyCode.A) => Main.controlerActor !UserMessage( LEFT, KeyPressEvent.RELEASED)
          case (KeyCode.D) => Main.controlerActor !UserMessage( RIGHT, KeyPressEvent.RELEASED)
          case (KeyCode.S) => Main.controlerActor !UserMessage( DOWN, KeyPressEvent.RELEASED)
          case (KeyCode.ESCAPE) => Main.controlerActor ! Exit
          case _ => ()
        }
      }
  }

  onHiding = (e: WindowEvent) =>  Main.controlerActor ! Exit
}
