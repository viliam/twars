package sk.kave.tank.fx.map

import collection._
import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite, JUnitRunner}
import org.junit.{Assert, Test, Before}
import collection.mutable.ListBuffer
import sk.kave.tank.Config
import sk.kave.tank.fx.{LEFT, RIGHT, UP, DOWN}
import scala.Some
import sk.kave.tank.utils.Logger
import sk.kave.tank.helpers.GameTestContext


/**
 * @autor: igor & vilo
 */
//@RunWith(classOf[JUnitRunner])
class MapViewTest extends JUnitSuite with ShouldMatchersForJUnit with Logger {

  implicit val gTestContext = new GameTestContext {
    override val config :Config = new Config() {
      def width = 4
      def height = 4
      def itemSize = 1
      def tankSize = 2
    }
  }

  import gTestContext._
  import gTestContext.config._
  var mapView: MapView[Int] = null

  def rows = mapView.rows
  def cols = mapView.cols
  def BORDER_SIZE = mapView.BORDER_SIZE


  @Before def before() {
    mapView = new MapView[Int] (
      (oI: Option[Int], x: Int, y: Int) => x
    )
    mapView.init()
    debug(mapView.rows.mkString("\n"), All)
  }

  @Test def testMapCreation() {
    assertMap
  }


  @Test def testMoveDown() {
    mapView.move(Some(DOWN))
    assertMap
  }

  @Test def testMoveUpDown() {
    mapView.move(Some(DOWN))
    mapView.move(Some(UP))

    assertMap
  }

  @Test def testMoveLeft() {
    mapView.move(Some(RIGHT))

    assertMap(
      Vector(299, 300, 301, 302, 303, 304, 298),
      Vector(399, 400, 401, 402, 403, 404, 398))
  }

  @Test def testMoveLeftRight() {
    mapView.move(Some(RIGHT))
    mapView.move(Some(LEFT))

    assertMap
  }

  @Test def testMoveSpiral() {
    mapView.move(Some(DOWN))
    mapView.move(Some(RIGHT))
    mapView.move(Some(UP))
    mapView.move(Some(LEFT))

    assertMap
  }

  private def assertMap  {
    val row = mapView.row
    val col = mapView.col
    val rangeRow: Range = row to row + width + (2*BORDER_SIZE)
    val rangeCol: Range = col to col + height + (2*BORDER_SIZE)

    assertMap( rangeRow, rangeCol)
  }

  private def assertMap( expectedRow : IndexedSeq[Int], expectedCol : IndexedSeq[Int]) {
    //assert rows
    for (i <- expectedRow) {
      assert(rows.contains(i), "Row index [" + i + "] doesn't exist: " + rows.toString() )
      assert(expectedCol.toArray === rows(i).toArray, "On index [" + i + "] " +
        "\nrow(index) = " + rows(i).toString() +
        "\nexpected = " + expectedCol.toString() )
    }

    //assert cols
    assert(cols != null)
    for (i <- expectedCol) {
      assert(cols.contains(i), "Column index [" + i + "] doesn't exist: " + cols.toString() )
      assert(cols(i).toList.forall(i == _))
    }

    assert(height + 2*BORDER_SIZE +1 === rows.size) // todo why +1 ? maybe bug
    assert(width + 2*BORDER_SIZE +1 === cols.size)
  }


}
