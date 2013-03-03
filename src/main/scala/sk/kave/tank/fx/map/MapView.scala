package sk.kave.tank.fx.map

import sk.kave.tank._
import beans.{Items, Game}
import collection.mutable
import sk.kave.tank.fx._
import collection.immutable.IndexedSeq
import collection.mutable.ListBuffer
import scala.Some
import utils.Logger

/**
 * Created by IntelliJ IDEA.
 * User: Igo || Vil
 * Date: 18.2.2013
 * Time: 11:55
 */

class MapView[R](val initRec: (Option[R], Int, Int) => R)(implicit config: Config) extends Logger{

  import config._

  val BORDER_SIZE = 1 //width of border (in rectangles) around user's view

  val map = Game.map
  val tank = Game.tank

  //current position of the map group; coordinates are indices in map data model
  var col = tank.x - BORDER_SIZE - (config.width - tankSize) / 2
  var row = tank.y - BORDER_SIZE - (config.height - tankSize) / 2

  def colMax = config.width + col + BORDER_SIZE * 2

  def rowMax = config.height + row + BORDER_SIZE * 2

  val cols = mutable.Map() ++ (for (i <- col to colMax) yield {
    (i, new ListBuffer[R]())
  })
  val rows = mutable.Map() ++ (for (i <- row to rowMax) yield {
    (i, new ListBuffer[R]())
  })

  def init(): IndexedSeq[R] = {

    for (
      iCol <- col to colMax;
      iRow <- row to rowMax)
    yield {
      val r = initRec(None, iCol, iRow)

      rows(iRow) += r
      cols(iCol) += r
      r
    }
  }

  def canMove(vect: Vector2D) = map.canMove( (col, row), (BORDER_SIZE + config.width, BORDER_SIZE + config.height), vect)

  def move(d: Option[Direction]) {
//    debug("move to direction = " + d + "  on row: " + row + "; col:" + col, Igor)

    d match {
      case Some(DOWN) => {
        require(row >= 0 - BORDER_SIZE && row < map.maxRows - 1 + BORDER_SIZE, "row = " + row + " maxRows = " + map.maxRows)
        require(col >= 0 - BORDER_SIZE && col < map.maxCols + BORDER_SIZE, "col = " + col + " maxCols = " + map.maxCols)

        moveVertical(row, rowMax + 1, DOWN)

        row = row + 1
      }
      case Some(UP) => {
        require(row >= 1 - BORDER_SIZE && row < map.maxRows + BORDER_SIZE, "row = " + row + " maxRows = " + map.maxRows)
        require(col >= 0 - BORDER_SIZE && col < map.maxCols + BORDER_SIZE, "col = " + col + " maxCols = " + map.maxCols)

        moveVertical(rowMax, row - 1, UP)

        row = row - 1
      }
      case Some(RIGHT) =>
        require(row >= 0 - BORDER_SIZE && row < map.maxRows + BORDER_SIZE, "row = " + row + " maxRows = " + map.maxRows)
        require(col >= 0 - BORDER_SIZE && col < map.maxCols - 1 + BORDER_SIZE, "col = " + col + " maxCols = " + map.maxCols)

        moveHorizontal(col, colMax + 1, RIGHT)

        col = col + 1
      case Some(LEFT) =>
        require(row >= 0 - BORDER_SIZE && row < map.maxRows + BORDER_SIZE, "row = " + row + " maxRows = " + map.maxRows)
        require(col >= 1 - BORDER_SIZE && col < map.maxCols + BORDER_SIZE, "col = " + col + " maxCols = " + map.maxCols)

        moveHorizontal(colMax, col - 1, LEFT)

        col = col - 1
      case None =>
    }
  }

  private def moveHorizontal(from: Int, to: Int, direction: Horizontal) {
    moveEveryEdgeFromTo(rows, direction == RIGHT)
    val li = moveEdgeFromTo(cols, from, to)
    require(!li.isEmpty)

    val list = li.get
    for (i <- 0 until list.size) {
      initRec(Some(list(i)), to, row + i)
    }
  }

  private def moveVertical(from: Int, to: Int, direction: Vertical) {
    moveEveryEdgeFromTo(cols, direction == DOWN)
    val li = moveEdgeFromTo(rows, from, to)
    require(!li.isEmpty)

    val list = li.get
    for (i <- 0 until list.size) {
      initRec(Some(list(i)), col + i, to)
    }
  }


  private def moveEdgeFromTo(map: mutable.Map[Int, ListBuffer[R]], oldPosition: Int, newPosition: Int): Option[ListBuffer[R]] = {
    val liOption = map.remove(oldPosition) //remove rectangles from their old position

    map(newPosition) = liOption.get //move them to new position

    liOption
  }

  private def moveEveryEdgeFromTo(map: mutable.Map[Int, ListBuffer[R]], dir: Boolean) {

    if (dir) {
      for (k <- map.keys) {
        val firstRect = map(k).remove(0)
        map(k) += firstRect
      }
    } else {
      for (k <- map.keys) {
        val firstRect = map(k).remove(map(k).size - 1)
        firstRect +=: map(k)
      }
    }
  }

  def updateRec(uCol : Int, uRow: Int, newItem: Items) {
    if (row >= uRow && col >= uCol && row < rowMax && col < colMax) return
    var r = rows(uRow)(uCol - col)

    //update JFX
    r = initRec(Some(r), uCol, uRow)

    //update rows map
    rows(uRow)(uCol - col) = r

    //update cols map
    cols(uCol)(uRow - row) = r
  }
}
