package sk.kave.tank.beans

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.events.MapChangeEvent

/**
 * @author Igo
 */
class MapImplTest extends FlatSpec with ShouldMatchers {

  "Map " should " fire event when changed" in {
    val constructor = classOf[MapImpl].getConstructors()(0)
    constructor.setAccessible(true)
    val args: COLUMNS = (Array(Array(Stone, Grass, Ground)))
    val instance = constructor.newInstance(args).asInstanceOf[Map]

    instance.addListener(42, e => {
      e should not be null
      e.isInstanceOf[MapChangeEvent] should equal(true)

      val mE = e.asInstanceOf[MapChangeEvent]
      mE.row should equal(1)
      mE.col should equal(0)
      mE.newValue should be(Stone)
    }
    )

    //change map
    instance(0, 1) should not be (Stone)
    instance(0, 1) = Stone
    instance(0, 1) should be(Stone)
  }
}
