package sk.kave.tank.beans

import scalafx.scene.paint.Color

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:17 PM
 */

object Items {
  def apply(charCode: Char) : Items = {
    charCode match {
      case '1' => Grass
      case '0' => Stone
      case '7' => Ground
      case c @ _   => throw new RuntimeException(" Find char '" + c + "' is not supported items")
    }
  }
}

sealed abstract class Items(val fillColor:Color)



case object Ground extends Items(Color.BROWN)
case object Grass extends Items(Color.GREEN)
case object Stone extends Items(Color.GREY)
case object NoMap extends Items(Color.WHITE)

