package sk.kave.tank.fx.map

import collection._
import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import org.junit.{Test, Before}
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
    override val config: Config = new Config() {
      def width = 4

      def height = 4

      def itemSize = 1

      def tankSize = 2
    }
  }

  case class MutableInt(var value: Int)

  import gTestContext.config._

  var mapView: MapView[MutableInt] = null //MapView must work with mutable types

  def rows = mapView.rows

  def cols = mapView.cols

  def BORDER_SIZE = mapView.BORDER_SIZE

  var rectValue: Option[Int] = None


  @Before def before() {
    mapView = new MapView[MutableInt](
      (oI: Option[MutableInt], x: Int, y: Int) => {
        val newVal: Int = if (rectValue.isDefined) rectValue.get else x //simple hack to influence initRec function; mocking is very difficult and reflection is extremly difficult
        oI match {
          case Some(mutableI) =>
            mutableI.value = newVal
            mutableI
          case _ =>
            MutableInt(newVal)
        }
      }

    )

    rectValue = None

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

  @Test def testMoveUp() {
    mapView.move(Some(UP))
    assertMap
  }

  @Test def testMoveUpDown() {
    mapView.move(Some(DOWN))
    mapView.move(Some(UP))

    assertMap
  }

  @Test def testMoveLeft() {
    mapView.move(Some(UP))

    assertMap
  }

  @Test def testMoveRight() {
    mapView.move(Some(LEFT))

    assertMap
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

  /**
   * map update should not happen, when updating outside MapView boundaries
   * map consists of Ints (MutableInts, to be precise) -> so to test if there was any map change (update),
   * I simply calculate sum of all these Ints before and after map update
   * if they are the same, no update happened and that is OK
   */
  @Test
  def testUpdateMap_boundaries() {

    val row = mapView.row
    val col = mapView.col
    val rowMax = mapView.rowMax
    val colMax = mapView.colMax
    val originalRowsSum = rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum)
    val originalColsSum = cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum)

    rectValue = Some(99)

    mapView.updateRec(col - 1, row)

    originalRowsSum should equal(rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
    originalColsSum should equal(cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))

    mapView.updateRec(col, row - 1)

    originalRowsSum should equal(rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
    originalColsSum should equal(cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))

    mapView.updateRec(colMax+1, row)

    originalRowsSum should equal(rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
    originalColsSum should equal(cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))

    mapView.updateRec(col, rowMax+1)

    originalRowsSum should equal(rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
    originalColsSum should equal(cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
  }


  @Test
  def testUpdateMap() {

    val row = mapView.row
    val col = mapView.col
    val rowMax = mapView.rowMax
    val colMax = mapView.colMax
    val originalRowsSum = rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum)
    val originalColsSum = cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum)

    rectValue = Some(99)
    //only checking if everything is initialized correctly
    mapView.rows(row + 1)(col + 1 - col) should not equal (MutableInt(rectValue.get))
    mapView.cols(col + 1)(row + 1 - row) should not equal (MutableInt(rectValue.get))

    mapView.updateRec(col + 1, row + 1)

    mapView.rows(row + 1)(col + 1 - col) should equal(MutableInt(rectValue.get))
    mapView.cols(col + 1)(row + 1 - row) should equal(MutableInt(rectValue.get))
    originalRowsSum should not equal (rows.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
    originalColsSum should not equal (cols.foldLeft(0)((sum, values) => sum + values._2.map(convertToInt).sum))
  }

  private implicit def convertToInt(mutableI: MutableInt): Int = mutableI.value

  private implicit def convertToMutableInt(i: Int): MutableInt = MutableInt(i)

  private implicit def convertToIntArray(mutableIArray: Array[MutableInt]): Array[Int] = mutableIArray.map(convertToInt)


  private def assertMap {
    val row = mapView.row
    val col = mapView.col
    val rangeRow: Range = row to row + width + (2 * BORDER_SIZE)
    val rangeCol: Range = col to col + height + (2 * BORDER_SIZE)

    assertMap(rangeRow, rangeCol)
  }

  private def assertMap(expectedRow: IndexedSeq[Int], expectedCol: IndexedSeq[Int]) {
    //assert rows
    assert(rows != null)
    for (i <- expectedRow) {
      assert(rows.contains(i), "Row index [" + i + "] doesn't exist: " + rows.toString())
      assert(expectedCol.toArray === convertToIntArray(rows(i).toArray), "On index [" + i + "] " +
        "\nrow(index) = " + rows(i).toString() +
        "\nexpected = " + expectedCol.toString())
    }

    //assert cols
    assert(cols != null)
    for (i <- expectedCol) {
      assert(cols.contains(i), "Column index [" + i + "] doesn't exist: " + cols.toString())
      assert(convertToIntArray(cols(i).toArray).forall(i == _))
    }

    assert(height + 2 * BORDER_SIZE + 1 === rows.size) // todo why +1 ? maybe bug
    assert(width + 2 * BORDER_SIZE + 1 === cols.size)
  }
}
