package sk.kave.tank.fx.map

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.beans.{NoMap}
import sk.kave.tank.GameContext
import sk.kave.tank.events.MapEvent
import sk.kave.tank.helpers.GameTestContext

/**
 * @autor vilo & igor
 */

@RunWith(classOf[JUnitRunner])
class MapGroupTest extends FlatSpec with ShouldMatchers {

  implicit object gC extends GameTestContext

  //todo!!! this is not unit test, but integration
  //what are you testing here? mapGroup? mapView or map?
  //choose one and from another make mock objects
  "A mapGroup " should " react of map change event " in {
    val newItem = NoMap

    val mapGroup = new MapGroup()(gC)

    mapGroup.init()

    val c = gC.mapWidth /2
    val r = gC.mapHeight /2

    val recOld = gC.map(c, r)
    require(recOld != newItem)

    gC.map.addListener(42, (event: MapEvent) => {
      val recNew = gC.map(c, r)

      recNew should not equal (recOld)
      recNew should equal (newItem)
    })

    gC.map(c, r) = newItem
    newItem.fillColor should equal (mapGroup.mapView.rows(r)(c - mapGroup.mapView.col).fill.value)
    newItem.fillColor should equal (mapGroup.mapView.cols(c)(r - mapGroup.mapView.row).fill.value)
  }
}
