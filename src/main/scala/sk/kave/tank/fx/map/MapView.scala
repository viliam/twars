package sk.kave.tank.fx.map

import sk.kave.tank._
import collection.mutable
import fx._
import scalafx.scene.shape.Rectangle
import collection.immutable.IndexedSeq
import java.util
import scala.Some
import scala.Some
import scala.Some

/**
 * Created by IntelliJ IDEA.
 * User: Igo || Vil
 * Date: 18.2.2013
 * Time: 11:55
 */
class MapView {
  val BORDER_SIZE = 1 //width of border (in rectangles) around user's view

  //current position of the map group; coordinates are indices in map data model
  var col = 0
  var row = 0

  def colMax = Width / ItemSize + col - 1

  def rowMax = Height / ItemSize + row - 1

  val rows = mutable.Map() ++ (for (i <- col to colMax) yield {
    (i, new util.LinkedList[Rectangle]())
  })
  val cols = mutable.Map() ++ (for (i <- row to rowMax) yield {
    (i, new util.LinkedList[Rectangle]())
  })

  def init(): IndexedSeq[Rectangle] = {

    for (
      iCol <- col to colMax;
      iRow <- row to rowMax)
    yield {
      val r = MapGroup.initRec(new Rectangle() {
        width = ItemSize + 2
        height = ItemSize + 2
      }, iCol, iRow)

      rows(iRow).addLast(r)
      cols(iCol).addLast(r)
      r
    }
  }

  def canMove(dir: (Option[Horizontal], Option[Vertical])): Boolean = {

    (dir._1 match {
      case Some(LEFT) if (col <= 0) =>
        false
      case Some(RIGHT) if (col == MapGroup.map.maxCols - 1) =>
        false
      case _ => true
    }) &&
      (dir._2 match {
        case Some(UP) if (row <= 0) =>
          false
        case Some(DOWN) if (row == MapGroup.map.maxRows - 1) =>
          false
        case _ => true
      })
  }

  def move(d: Option[Direction]) {
    logg.debug("move to direction = " + d + "  on row: " + row + "; col:" + col)

    d match {
      case Some(DOWN) => {
        require(row >= 0 && row < MapGroup.map.maxRows - 1)
        require(col >= 0 && col < MapGroup.map.maxCols)

        moveVertical(row, rowMax + 1, DOWN)

        row = row + 1
      }
      case Some(UP) => {
        require(row >= 1 && row < MapGroup.map.maxRows)
        require(col >= 0 && col < MapGroup.map.maxCols)

        moveVertical(rowMax, row - 1, UP)

        row = row - 1
      }
      case Some(RIGHT) =>
        require(row >= 0 && row < MapGroup.map.maxRows)
        require(col >= 0 && col < MapGroup.map.maxCols - 1)

        moveHorizontal(col, colMax + 1, RIGHT)

        col = col + 1
      case Some(LEFT) =>
        require(row >= 0 && row < MapGroup.map.maxRows)
        require(col >= 1 && col < MapGroup.map.maxCols)

        moveHorizontal(colMax, col - 1, LEFT)

        col = col - 1
      case None =>
    }
  }

  private def moveHorizontal(from: Int, to: Int, direction: Horizontal) {
    moveEveryEdgeFromTo(rows, direction == RIGHT)
    val li = moveEdgeFromTo(cols, from, to)
    require(!li.isEmpty)


    for (i <- 0 until li.get.size) {
      val r = li.get.get(i)
      require(r.y.toInt == (row + i) * ItemSize, " Zamiesalo sa ti to, kaaaaamo,.. Y [" + i + "," + row + "]  " + r.y() + "  === " + ((row + i) * ItemSize))

      MapGroup.initRec(r, to, row + i)
    }
  }

  private def moveVertical(from: Int, to: Int, direction: Vertical) {
    moveEveryEdgeFromTo(cols, direction == DOWN)
    val li = moveEdgeFromTo(rows, from, to)
    require(!li.isEmpty)


    for (i <- 0 until li.get.size) {
      val r = li.get.get(i)
      require(r.x.toInt == (col + i) * ItemSize, " Zamiesalo sa ti to, kaaaaamo,..X [" + i + "," + col + "]  " + r.x() + "  === " + ((col + i) * ItemSize))

      MapGroup.initRec(li.get.get(i), col + i, to)
    }
  }


  private def moveEdgeFromTo(map: mutable.Map[Int, util.LinkedList[Rectangle]], oldPosition: Int, newPosition: Int): Option[util.LinkedList[Rectangle]] = {
    logg.debug("move edge, oldposition = " + oldPosition + "    newposition = " + newPosition)

    val liOption = map.remove(oldPosition) //remove rectangles from their old position

    map(newPosition) = liOption.get //move them to new position

    liOption
  }

  private def moveEveryEdgeFromTo(map: mutable.Map[Int, util.LinkedList[Rectangle]], dir: Boolean) {

    if (dir) {
      for (k <- map.keys) {
        val firstRect = map(k).removeFirst()
        map(k).addLast(firstRect)
      }
    } else {
      for (k <- map.keys) {
        val firstRect = map(k).removeLast()
        map(k).addFirst(firstRect)
      }
    }
  }

  def printAll = {
    println("\n COLS : ")
    for (k <- cols.keys) {
      println()
      for (i <- 0 until cols(k).size;
           r = cols(k).get(i)) {
        print("[" + (r.x.toInt / ItemSize) + "," + (r.y.toInt / ItemSize) + "]")
      }
    }
    println("\n ROWS: ")
    for (k <- rows.keys) {
      println()
      for (i <- 0 until rows(k).size;
           r = rows(k).get(i)) {
        print("[" + (r.x.toInt / ItemSize) + "," + (r.y.toInt / ItemSize) + "]")
      }
    }
  }
}
