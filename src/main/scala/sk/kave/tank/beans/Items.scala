/*
 * Copyright
 *  viliam.kois@gmail.com   Kois Viliam
 *  ikvaso@gmail.com        Kvasnicka Igor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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

