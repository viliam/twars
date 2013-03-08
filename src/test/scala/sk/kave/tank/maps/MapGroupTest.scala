package sk.kave.tank.maps

import org.junit.{Assert, Test, Before}
import sk.kave.tank.fx.map.MapGroup
import sk.kave.tank.beans.NoMap
import sk.kave.tank.events.MapChangeEvent
import sk.kave.tank._

/**
 * @author Igo
 */
class MapGroupTest {

  @Before
  def before() {
    MapGroup.init()
  }

  @Test
  def testMapChangeEvent() {
    val newItem = NoMap

    val gC = GameContext
    val recOld = gC.map(2, 2)
    require(recOld != newItem)

    gC.map.addListener(42, (event: MapChangeEvent) => {
      Assert.assertEquals(2, event.col)
      Assert.assertEquals(2, event.row)

      val recNew = gC.map(2, 2)

      Assert.assertFalse(recNew == recOld)
      Assert.assertTrue(recNew == newItem)
    })

    gC.map(2, 2) = newItem
    Assert.assertEquals(newItem.fillColor, MapGroup.mapView.rows(2)(2).fill.value)
    Assert.assertEquals(newItem.fillColor, MapGroup.mapView.cols(2)(2).fill.value)
  }
}
