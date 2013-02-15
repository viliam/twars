package sk.kave.tank.fx

import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.scene._
import shape.Rectangle

import javafx.scene.paint.Color
import sk.kave.tank.beans._
import sk.kave.tank._
import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.stage.WindowEvent

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:11 PM
 */
class MapStage extends Stage {

  title = "ScalaFX Tetris"

  val map = Map()

  val mapGroup = new Group() {
    children =
      for (
        iCol <- 0 until map.items.length;
        iRow <- 0 until map.items(iCol).length)
      yield {
        new Rectangle() {
          width = ItemSize
          height = ItemSize
          x = iCol * ItemSize
          y = iRow * ItemSize

          fill = map.items(iCol)(iRow) match {
            case Grass => Color.GREEN
            case Stone => Color.GRAY
            case Ground => Color.BROWN
          }
        }
      }
  }

  val controlerActor = (new GameControlerActor(mapGroup)).start()

  scene = new Scene(200, 200) {
    fill = Color.BLACK
    content = List(
      mapGroup
    )

    onKeyPressed = new EventHandler[KeyEvent] {
      def handle(e: KeyEvent) {
        e.code match {
          case (KeyCode.UP) => controlerActor ! Action.UP
          case (KeyCode.LEFT) => controlerActor ! Action.LEFT
          case (KeyCode.RIGHT) => controlerActor ! Action.RIGHT
          case (KeyCode.DOWN) => controlerActor ! Action.DOWN
          case _ => ()
        }
      }
    }

    onKeyReleased = new EventHandler[KeyEvent] {
      def handle(e: KeyEvent) {
        e.code match {
          case (KeyCode.UP) => controlerActor ! Action.UP
          case (KeyCode.LEFT) => controlerActor ! Action.LEFT
          case (KeyCode.RIGHT) => controlerActor ! Action.RIGHT
          case (KeyCode.DOWN) => controlerActor ! Action.DOWN
          case (KeyCode.ESCAPE) => controlerActor ! Action.EXIT
          case _ => ()
        }
      }
    }
  }

  onHiding = new EventHandler[WindowEvent] {
    def handle(e: WindowEvent) {
      controlerActor ! Action.EXIT
    }
  }
}
