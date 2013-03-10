package sk.kave.tank.maps

import org.junit.{Assert, Test, Before}
import sk.kave.tank.fx.map.MapView
import sk.kave.tank.fx._
import sk.kave.tank.Config
import collection.mutable.ListBuffer
import sk.kave.tank.beans.Game
import javafx.util.Duration

/**
 * @author Igo
 */
class MapViewTest {
  var mapView: MapView[Int] = null

  val testConfig = new Config() {
    def width = 4
    def height = 4
    def itemSize = 1
    def tankSize = 2
    def tankRotationDuration:Duration = 10 ms
    def tankMovementDuration:Duration = 10 ms
  }

  implicit val testGameContext = new Game {
    override val config = testConfig
  }

  def rows = mapView.rows

  def cols = mapView.cols


  @Before
  def before() {
    mapView = new MapView[Int](
      (oI: Option[Int], x: Int, y: Int) => {
        x
      })
    mapView.init()
    logg.debug(mapView.rows.mkString("\n"))
  }

  @Test
  def testMapCreation() {
    assertMap(rows, cols)
  }

  @Test
  def testMoveDown() {
    mapView.move(Some(DOWN))

    //assert rows
    Assert.assertNotNull(rows)
    for (i <- 0 - mapView.BORDER_SIZE + 1 until 5 - mapView.BORDER_SIZE + 1) {
      Assert.assertTrue(rows.contains(i))
      Assert.assertArrayEquals((0 - mapView.BORDER_SIZE until 5 - mapView.BORDER_SIZE).toArray, rows(i).toArray)
    }

    //assert cols
    Assert.assertNotNull(cols)
    for (i <- 0 - mapView.BORDER_SIZE until 4 - mapView.BORDER_SIZE) {
      Assert.assertTrue(cols.contains(i))
      Assert.assertTrue(cols(i).toList.forall(i == _))
    }

    Assert.assertEquals(4 + mapView.BORDER_SIZE, rows.size)
    Assert.assertEquals(4 + mapView.BORDER_SIZE, cols.size)
  }

  @Test
  def testMoveUpDown() {
    mapView.move(Some(DOWN))
    mapView.move(Some(UP))

    assertMap(rows, cols)
  }

  @Test
  def testMoveLeft() {
    mapView.move(Some(RIGHT))

    //assert rows
    Assert.assertNotNull(rows)
    for (i <- 0 until 4) {
      Assert.assertTrue(rows.contains(i))
      Assert.assertArrayEquals(Array(0, 1, 2, 3, -1), rows(i).toArray)
    }

    //assert cols
    Assert.assertNotNull(cols)
    for (i <- 0 - mapView.BORDER_SIZE + 1 until 4 - mapView.BORDER_SIZE + 1) {
      Assert.assertTrue(cols.contains(i))
      Assert.assertTrue(cols(i).toList.forall(i == _))
    }

    Assert.assertTrue(cols(cols.size - 1).toList.forall(-1 == _))

    Assert.assertEquals(4 + mapView.BORDER_SIZE, rows.size)
    Assert.assertEquals(4 + mapView.BORDER_SIZE, cols.size)
  }

  @Test
  def testMoveLeftRight() {
    mapView.move(Some(RIGHT))
    mapView.move(Some(LEFT))

    assertMap(rows, cols)
  }

  @Test
  def testMoveSpiral() {
    mapView.move(Some(DOWN))
    mapView.move(Some(RIGHT))
    mapView.move(Some(UP))
    mapView.move(Some(LEFT))

    assertMap(rows, cols)
  }

  private def assertMap(rows: scala.collection.mutable.Map[Int, ListBuffer[Int]], cols: scala.collection.mutable.Map[Int, ListBuffer[Int]]) {
    //assert rows
    for (i <- 0 - mapView.BORDER_SIZE until 5 - mapView.BORDER_SIZE) {
      Assert.assertTrue(rows.contains(i))
      Assert.assertArrayEquals((0 - mapView.BORDER_SIZE until 5 - mapView.BORDER_SIZE).toArray, rows(i).toArray)
    }


    //assert cols
    Assert.assertNotNull(cols)
    for (i <- 0 - mapView.BORDER_SIZE until 5 - mapView.BORDER_SIZE) {
      Assert.assertTrue(cols.contains(i))
      Assert.assertTrue(cols(i).toList.forall(i == _))
    }

    Assert.assertEquals(4 + mapView.BORDER_SIZE, rows.size)
    Assert.assertEquals(4 + mapView.BORDER_SIZE, cols.size)
  }
}
