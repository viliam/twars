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

import sk.kave.tank.utils.{Logger, Vector2D}

/**
 * User: vilo
 */
object Bullet {
  val BULLET_DIFFERENCE = 5
}

class Bullet(val direction: Vector2D) extends Logger{

  private var _distanceFromSource: Int = 0

  def canCreateNewBullet: Boolean = {
    _distanceFromSource = _distanceFromSource + 1
    return (_distanceFromSource == Bullet.BULLET_DIFFERENCE)
  }

  def distanceFromSource = _distanceFromSource
}
