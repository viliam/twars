package sk.kave.tank.beans

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */
import sk.kave.tank._


object Map {
   def apply() = {
     readMapFromFile("mapa.mapa")
   }
}

class Map ( val items : ROWS ){


}
