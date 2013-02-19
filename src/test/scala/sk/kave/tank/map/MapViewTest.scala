package sk.kave.tank.map

import org.junit.{Assert, Test, Before}
import sk.kave.tank.fx.map.MapView
import sk.kave.tank.fx._
import sk.kave.tank.Config

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
        x + y
      })(testConfig)
    mapView.init()

    Assert.assertEquals(4,rows.size)
    Assert.assertEquals(4,cols.size)
  }

  @Test
  def testMoveDown() {
    println(mapView.rows.mkString("\n"))

    Assert.assertFalse(rows.contains(rows.size))
    Assert.assertTrue(rows.contains(0))

    mapView.move(Some(DOWN))
    println()
    println(mapView.rows.mkString("\n"))
    Assert.assertNotNull(rows)
    Assert.assertTrue(rows.contains(rows.size))
    Assert.assertFalse(rows.contains(0))
  }
}
