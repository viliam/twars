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

import sk.kave.tank._
import beans.{IGameContext, Map, Items, GameContextImpl}
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

class MapView[R](val initRec: (Option[R], Int, Int) => R)
                (implicit gContext: IGameContext) extends Logger{


  import gContext._
  import gContext.config._


  val BORDER_SIZE = 1 //width of border (in rectangles) around user's view

  //current position of the map group; coordinates are indices in map data model
  var col = initG.tankX - BORDER_SIZE - (config.width - tankSize) / 2
  var row = initG.tankY - BORDER_SIZE - (config.height - tankSize) / 2

  def colMax = config.width + col + BORDER_SIZE * 2
  def rowMax = config.height + row + BORDER_SIZE * 2

  val cols = mutable.Map() ++ (for (i <- col to colMax) yield {
    (i, new ListBuffer[R]())
  })
  val  rows = mutable.Map() ++ (for (i <- row to rowMax) yield {
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
//    debug("map move to direction = " + d + "  on row: " + row + "; col:" + col, Igor)

    
    d match {
      case Some(DOWN) => {
        require(row >= 0 - BORDER_SIZE && row < mapHeight - 1 + BORDER_SIZE, "row = " + row + " maxRows = " + mapHeight)
        require(col >= 0 - BORDER_SIZE && col < mapWidth + BORDER_SIZE, "col = " + col + " maxCols = " + mapWidth)

        moveVertical(row, rowMax + 1, DOWN)

        row = row + 1
      }
      case Some(UP) => {
        require(row >= 1 - BORDER_SIZE && row < mapHeight + BORDER_SIZE, "row = " + row + " maxRows = " + mapHeight)
        require(col >= 0 - BORDER_SIZE && col < mapWidth + BORDER_SIZE, "col = " + col + " maxCols = " + mapWidth)

        moveVertical(rowMax, row - 1, UP)

        row = row - 1
      }
      case Some(RIGHT) =>
        require(row >= 0 - BORDER_SIZE && row < mapHeight + BORDER_SIZE, "row = " + row + " maxRows = " + mapHeight)
        require(col >= 0 - BORDER_SIZE && col < mapWidth - 1 + BORDER_SIZE, "col = " + col + " maxCols = " + mapWidth)

        moveHorizontal(col, colMax + 1, RIGHT)

        col = col + 1
      case Some(LEFT) =>
        require(row >= 0 - BORDER_SIZE && row < mapHeight + BORDER_SIZE, "row = " + row + " maxRows = " + mapHeight)
        require(col >= 1 - BORDER_SIZE && col < mapWidth + BORDER_SIZE, "col = " + col + " maxCols = " + mapWidth)

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
      initRec(Some(list(i)), to, row + i) //not very nice, because I am relying on the fact, that Rec is mutable, but this way I modify item in both maps: rows and cols
    }
  }

  private def moveVertical(from: Int, to: Int, direction: Vertical) {
    moveEveryEdgeFromTo(cols, direction == DOWN)
    val li = moveEdgeFromTo(rows, from, to)
    require(!li.isEmpty)

    val list = li.get
    for (i <- 0 until list.size) {
      initRec(Some(list(i)), col + i, to)   //not very nice, because I am relying on the fact, that Rec is mutable, but this way I modify item in both maps: rows and cols
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

  def updateRec(uCol : Int, uRow: Int) {
    if ((row >= uRow || col >= uCol || uRow > rowMax || uCol > colMax)) return
    var r = rows(uRow)(uCol - col)

    //update JFX
    r = initRec(Some(r), uCol, uRow)

    //update rows map
    rows(uRow)(uCol - col) = r

    //update cols map
    cols(uCol)(uRow - row) = r
  }
}
