package sk.kave.tank.maps

import org.junit.{Ignore, Test}
import sk.kave.tank._
import beans.{Grass, Items, Stone}

/**
 * @author Igo
 */
class MapImplTest {

  @Ignore
  @Test
  def testCanBulletMove() { //this is not a test, just a main() method
    val map: Map = new Map(Array(
      Array(Grass, Grass, Grass, Grass, Grass),
      Array(Grass, Stone, Stone, Stone, Grass),
      Array(Grass, Grass, Grass, Grass, Grass)
    ))
    val xx: Double = 1.5
    val yy: Double = 2.7

    for (i <- getNeighbourItems(xx, yy, map)) {
      if (i == Stone) {
        println("Stone")
      } else {
        println("grass")
      }
    }
  }

  //see MapImpl method getNeighbourItems()
  private def getNeighbourItems(x: Double, y: Double, map: Map): Array[Items] = {
    val x_es = Array(math.floor(x).toInt, math.ceil(x).toInt)
    val y_es = Array(math.floor(y).toInt, math.ceil(y).toInt)

    for (i <- x_es; j <- y_es) yield {
      map(i, j)
    }
  }

  class Map(map: Array[Array[Items]]) {
    def apply(r: Int, c: Int): Items = {
      map(r)(c)
    }
  }

}
