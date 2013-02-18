package sk.kave.tank.fx

import sk.kave.tank._
import collection.mutable
import scalafx.scene.shape.Rectangle
import collection.mutable.ListBuffer
import collection.immutable.IndexedSeq

/**
 * Created by IntelliJ IDEA.
 * User: Igo
 * Date: 18.2.2013
 * Time: 11:55
 */
class MapView {
  val BORDER_SIZE = 1 //width of border (in rectangles) around user's view

  //current position of the map group; coordinates are indices in map data model
  var col = 0
  var row = 0

  def colMax = Width / ItemSize + col - 1 + BORDER_SIZE * 2

  def rowMax = Height / ItemSize + row - 1 + BORDER_SIZE * 2

  val rows = mutable.Map() ++ (for (i <- col to colMax) yield {
    (i, ListBuffer[Rectangle]())
  })
  val cols = mutable.Map() ++ (for (i <- row to rowMax) yield {
    (i, ListBuffer[Rectangle]())
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

      rows(iRow) += r
      cols(iCol) += r
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

        moveVertical(row, rowMax + 1)

        row = row + 1
      }
      case Some(UP) => {
        require(row >= 1 && row < MapGroup.map.maxRows)
        require(col >= 0 && col < MapGroup.map.maxCols)

        moveVertical(rowMax, row - 1)

        row = row - 1
      }
      case Some(RIGHT) =>
        require(row >= 0 && row < MapGroup.map.maxRows)
        require(col >= 0 && col < MapGroup.map.maxCols - 1)

        moveHorizontal(col, colMax + 1)

        col = col + 1
      case Some(LEFT) =>
        require(row >= 0 && row < MapGroup.map.maxRows)
        require(col >= 1 && col < MapGroup.map.maxCols)

        moveHorizontal(colMax, col - 1)

        col = col - 1
      case None =>
    }
  }

  private def moveVertical(from: Int, to: Int) {
    val li = moveEdgeFromTo(rows, from, to)
    require(!li.isEmpty)
    moveEveryEdgeFromTo(cols, col, colMax)

    var colTemp = col
    li.get.foreach(rec => {
      MapGroup.initRec(rec, colTemp, to)
      colTemp = colTemp + 1
    })
  }

  private def moveHorizontal(from: Int, to: Int) {
    val li = moveEdgeFromTo(cols, from, to)
    require(!li.isEmpty)
    moveEveryEdgeFromTo(rows, row, rowMax)

    var rowTemp = row
    li.get.foreach(rec => {
      MapGroup.initRec(rec, to, rowTemp)
      rowTemp = rowTemp + 1
    })
  }

  private def moveEdgeFromTo(map: mutable.Map[Int, ListBuffer[Rectangle]], oldPosition: Int, newPosition: Int): Option[ListBuffer[Rectangle]] = {
    logg.debug("move edge, oldposition = " + oldPosition + "    newposition = " + newPosition)
    val liOption = map.remove(oldPosition) //remove rectangles from their old position

    map(newPosition) = liOption.get //move them to new position
    liOption
  }

  private def moveEveryEdgeFromTo(map: mutable.Map[Int, ListBuffer[Rectangle]], first: Int, last: Int) {
    for (i <- first until last) {
      val firstRect = map(i).remove(0) //TODO because of this consider using some kind of deque as optimalization; however I dont think current implementation (using ListBuffer) is "inefficient"
      map(i) += firstRect
    }
  }
}
