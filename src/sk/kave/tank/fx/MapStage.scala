package sk.kave.tank.fx

import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.scene._
import shape.Rectangle

import javafx.scene.paint.Color
import sk.kave.tank.beans._
import sk.kave.tank._
/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:11 PM
 */
class MapStage extends Stage {

  title = "ScalaFX Tetris"

  val map = Map()

  val boardGroup = new Group() {
    children =
      for (
        iCol  <- 0 until map.items.length;
        iRow <-  0 until map.items(iCol).length)
      yield
        new Rectangle() {
          width = ItemSize
          height =ItemSize
          x = iCol * ItemSize
          y = iRow * ItemSize

          fill = map.items(iCol)(iRow) match {
            case Grass => Color.GREEN
            case Stone => Color.GRAY
            case Ground => Color.BROWN
          }
        }
  }

  scene = new Scene(200 , 200) {
    fill = Color.BLACK
    content = List(
      boardGroup
    )
  }

}
