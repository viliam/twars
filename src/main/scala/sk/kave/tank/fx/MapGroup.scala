package sk.kave.tank.fx

import scalafx.scene.Group
import scalafx.scene.shape.Rectangle
import sk.kave.tank._
import beans.{Ground, Stone, Grass}
import scalafx.scene.paint.Color
import sk.kave.tank.beans.Map
import collection.mutable.ListBuffer
import collection._

object MapGroup extends Group {

  val map = Map()

  def col  = MapStage.x().toInt / ItemSize
  def row  = MapStage.y().toInt / ItemSize
  def colMax = Width / ItemSize + col
  def rowMax = Height / ItemSize + row

  val rows = mutable.Map() ++ (for ( i <- col until colMax)  yield (i, ListBuffer[ Rectangle]() ))
  val cols = mutable.Map() ++ (for ( i <- row until rowMax)  yield (i, ListBuffer[ Rectangle]() ))

  println (" col = " + col + "   row = " + row +  "   colM = " + colMax + "   rowM = " + rowMax)

  def init {
    children =
      for (
        iCol <- col until colMax;
        iRow <- row until rowMax)
      yield {
        val r = initRec (new Rectangle() {
          width = ItemSize + 2
          height = ItemSize + 2
        }, iCol, iRow)

//        rows(iRow)(iCol) = r       //TODO: fix add element to position
//        cols(iCol)(iRow) = r
        r
      }
  }

  private def initRec( rec : Rectangle, iCol : Int, iRow : Int) = {
    rec.x = iCol * ItemSize
    rec.y = iRow * ItemSize

    rec.fill = map(iCol,iRow) match {
      case Grass  => Color.GREEN
      case Stone  => Color.GRAY
      case Ground => Color.BROWN
    }
    rec
  }

  def move( d : Direction) =
    d match {
      case UP => {
        val li = cols.remove(row)       //move bottom line to upper
        cols(rowMax +1)= li.get

        for (i <- col until colMax) {    //in map of rows move every first rect. to the last position
          //val r = cols(i).next.remove()   //TODO : )

        }
      }
      case DOWN =>
      case LEFT =>
      case RIGHT =>
    }

}
