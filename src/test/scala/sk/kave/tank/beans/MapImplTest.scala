package sk.kave.tank.beans

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import sk.kave.tank.events.{ShootEvent, MapChangeEvent}
import sk.kave.tank.fx.{DOWN, RIGHT, LEFT, UP}
import sk.kave.tank.utils.Vector2D
import sk.kave.tank.helpers.ReflectionHelper

/**
 * @author Igo
 */
class MapImplTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach with ReflectionHelper {

  var mapInstance: Map = null

  override def beforeEach() {
    val constructor = classOf[MapImpl].getConstructors()(0)
    constructor.setAccessible(true)
    val args: COLUMNS = (Array(Array(Stone, Grass, Ground), Array(Grass, Stone, Ground), Array(Stone, Ground, Grass)))
    mapInstance = constructor.newInstance(args).asInstanceOf[Map]
  }


  "Map " should " not get value" in {
    mapInstance(0, 3) should equal(NoMap)
    mapInstance(0, -1) should equal(NoMap)
    mapInstance(3, 0) should equal(NoMap)
    mapInstance(-1, 0) should equal(NoMap)
  }

  "Map " should " get value" in {
    mapInstance(2, 2) should equal(Grass)
    mapInstance(0, 0) should equal(Stone)
    mapInstance(1, 1) should equal(Stone)
    mapInstance(1, 2) should equal(Ground)
  }

  "Map " should " fire event when changed" in {

    mapInstance.addListener(42, e => {
      //todo assert that this event is fired -> this is a problem, because actor is used to fire events, so events are asynchronous
      e should not be null
      e.isInstanceOf[MapChangeEvent] should equal(true)

      val mE = e.asInstanceOf[MapChangeEvent]
      mE.row should equal(1)
      mE.col should equal(0)
      mE.newValue should be(Stone)
    }
    )

    mapInstance(0, 1) = Stone
  }

  "Map " should " update when changed" in {

    //change map
    mapInstance(0, 1) should not be (Stone) //before map change
    mapInstance(0, 1) = Stone
    mapInstance(0, 1) should be(Stone)
  }

  "Map " should "check if move is possible" in {
    mapInstance.canMove((0, 0), (1, 1), (Some(RIGHT), None)) should be(true)
    mapInstance.canMove((0, 0), (1, 1), (Some(LEFT), None)) should be(false)

    mapInstance.canMove((3, 0), (1, 1), (Some(LEFT), None)) should be(true)
    mapInstance.canMove((3, 0), (1, 1), (Some(RIGHT), None)) should be(false)

    mapInstance.canMove((0, 0), (1, 1), (None, Some(DOWN))) should be(true)
    mapInstance.canMove((0, 0), (1, 1), (None, Some(UP))) should be(false)

    mapInstance.canMove((0, 3), (1, 1), (None, Some(DOWN))) should be(false)
    mapInstance.canMove((0, 3), (1, 1), (None, Some(UP))) should be(true)
  }

  "Shoot event " should " be fired" in {
    //todo assert that this event is fired -> this is a problem, because actor is used to fire events, so events are asynchronous
    //it would be also nice to test if bullet event invokes new bullet event - but I cannot do it without the problem in line above

    val shootEvent = ShootEvent(1, 2, new Bullet((Some(LEFT), Some(UP))), () => {})

    mapInstance.addListener(this, e => {
      e should not be null
      e.isInstanceOf[ShootEvent] should equal(true)
      val sE = e.asInstanceOf[ShootEvent]
      sE.x should equal(1)
      sE.y should equal(2)
      sE.bullet should not be null
      sE.bullet.direction should be((Some(LEFT), Some(UP)): Vector2D)
    })

    mapInstance.shoot(shootEvent)
  }

  "Bullet " should " check if move is possible" in {
    //todo finish when canBulletMove() will no longer work with Doubles, but Ints instead
    //mapInstance.callPrivateMethod("canBulletMove",)
  }
}
