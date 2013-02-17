package sk.kave.tank.fx

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._
import beans._
import scalafx.scene.paint.Color
import sk.kave.tank.beans.Map
import scala.collection.mutable.ListBuffer
import scalafx.animation.{KeyFrame, Timeline}
import javafx.event.{ActionEvent, EventHandler}
import scala._
import collection.mutable
import collection.Set
import scala.Some
import scalafx.Includes._
import scala.Some
import scala.Some

object MapGroup extends Group {

  val map = Map()

  //current position of the map group; coordinates are indices in map data model
  var col = 0
  var row = 0

  def colMax = Width / ItemSize + col

  def rowMax = Height / ItemSize + row

  val rows = mutable.Map() ++ (for (i <- col until colMax) yield {
    (i, ListBuffer[Rectangle]())
  })
  val cols = mutable.Map() ++ (for (i <- row until rowMax) yield {
    (i, ListBuffer[Rectangle]())
  })

  println(" col = " + col + "   row = " + row + "   colM = " + colMax + "   rowM = " + rowMax)

  def init() {
    children =
      for (
        iCol <- col until colMax;
        iRow <- row until rowMax)
      yield {
        val r = initRec(new Rectangle() {
          width = ItemSize + 2
          height = ItemSize + 2
        }, iCol, iRow)

        rows(iRow) += r
        cols(iCol) += r
        r
      }
  }

  /**
   * inits x,y and fill color
   */
  private def initRec(rec: Rectangle, iCol: Int, iRow: Int) = {
    rec.x = iCol * ItemSize
    rec.y = iRow * ItemSize

    rec.fill = map(iCol, iRow).fillColor
    rec
  }

  def move(d: Option[Direction]) {
    d match {
      case Some(UP) => {
        //------------update data model
        //move upper line to bottom
        val li = rows.remove(row)
        if (li.isEmpty) {
          //I am at the top - can't go higher than that
          return
        }
        rows(rowMax + 1) = li.get

        //in map of rows move every first rect. to the last position
        for (i <- col until colMax) {
          val firstRect = cols(i).remove(0) //TODO because of this consider using some kind of deque as optimalization; however I dont think current implementation (using ListBuffer) is "inefficient"
          cols(i) += firstRect
        }

        //------------update javaFX
        javafx.application.Platform.runLater(new Runnable() {
          def run() {
            var colTemp = col
            li.get.foreach(rec => {
              initRec(rec, colTemp, rowMax - 1)
              colTemp = colTemp + 1
            })
          }
        })

        row = row + 1
      }
      case Some(DOWN) =>
      case Some(LEFT) =>
      case Some(RIGHT) =>
      case None =>
    }
  }
}
