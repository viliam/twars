package sk.kave.tank.map

import org.junit.{Assert, Test, Before}
import sk.kave.tank.fx.map.MapGroup
import sk.kave.tank.events.mapchanged.MapChangedEvent
import sk.kave.tank.beans.NoMap

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

    val recOld = MapGroup.map(2, 2)
    require(recOld != newItem)

    MapGroup.map.addListener(42, (event: MapChangedEvent) => {
      Assert.assertEquals(2, event.col)
      Assert.assertEquals(2, event.row)

      val recNew = MapGroup.map(2, 2)

      Assert.assertFalse(recNew == recOld)
      Assert.assertTrue(recNew == newItem)
    })

    MapGroup.map(2, 2) = newItem
    Assert.assertEquals(newItem.fillColor, MapGroup.mapView.rows(2)(2).fill.value)
    Assert.assertEquals(newItem.fillColor, MapGroup.mapView.cols(2)(2).fill.value)
  }
}
