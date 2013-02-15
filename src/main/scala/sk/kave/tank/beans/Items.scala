package sk.kave.tank.beans

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

sealed abstract class Items
case object Ground extends Items
case object Grass extends Items
case object Stone extends Items

