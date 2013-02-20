package sk.kave.tank.map

import org.junit.{Assert, Test, Before}
import sk.kave.tank.fx.map.MapView
import sk.kave.tank.fx._
import sk.kave.tank.Config
import collection.mutable.ListBuffer

/**
 * @author Igo
 */
class MapViewTest {
  var mapView: MapView[Int] = null

  val testConfig = new Config() {
    def width = 4

    def height = 4

    def itemSize = 1
  }

  def rows = mapView.rows

  def cols = mapView.cols


  @Before
  def before() {
    mapView = new MapView[Int](
      (oI: Option[Int], x: Int, y: Int) => {
        x
      })(testConfig)
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

    Assert.assertNotNull(rows)
    Assert.assertTrue(rows.contains(rows.size))
    Assert.assertFalse(rows.contains(0))
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
    }

    //assert cols
    Assert.assertNotNull(cols)
    Assert.assertTrue(cols.contains(cols.size))
    Assert.assertFalse(cols.contains(0))
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
    Assert.assertNotNull(rows)
    for (i <- 0 until 4) {
      Assert.assertTrue(rows.contains(i))
    }

    //assert cols
    Assert.assertNotNull(cols)
    for (i <- 0 until 4) {
      Assert.assertTrue(cols(i).toList.forall(i == _))
    }

    Assert.assertEquals(4, rows.size)
    Assert.assertEquals(4, cols.size)
  }
}
