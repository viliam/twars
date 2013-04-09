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

/**
 * User: wilo
 * Date: 2/13/13
 * Time: 1:12 PM
 */

import sk.kave.tank._
import events.{ShootEvent, MapEvent,  EventListener}
import fx._
import utils.Logger
import java.io.{BufferedReader, File, FileReader}
import collection.mutable.ArrayBuffer

object Map extends Logger {

//  val items = readMapFromFile("mapaGround.mapa")
//  lazy val m: Map = new MapImpl( items)

  def apply(fileName :String) = {
    val items = readMapFromFile(fileName)
    new MapImpl( items)
  }


  private def readMapFromFile( fileName : String ) : COLUMNS = {
    val fileReader: FileReader = new FileReader(new File( fileName))
    val buffReader: BufferedReader = new BufferedReader(fileReader)

    var s: String = null
    val li: ArrayBuffer[Array[Items]] = new ArrayBuffer[Array[Items]]
    while ({s = buffReader.readLine; s} != null) {
      li += (for (a <- s) yield Items(a)).toArray
    }

    li.toArray
  }
}

trait Map extends EventListener[MapEvent] {

  def bound : (Int, Int)

  def apply(c: Int, r: Int): Items

  def update(c: Int, r: Int,  newValue: Items)

  /*
  For given position and bounds return if is posible move to specific direction
   */
  def canMove(position : => (Int, Int),
              bounds   : => (Int, Int),
              direction: => Vector2D): Boolean

  def shoot(e : ShootEvent)(implicit gContext : GameContextImpl)

}
