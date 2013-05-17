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

package sk.kave.tank.utils

import org.apache.log4j.{Logger=>Log4jLogger}
import collection.mutable.ListBuffer

object LoggerObj {
  var loggUser: String = null
}

trait Logger {

  val mf = implicitly[Manifest[this.type]]
  private val prefix = "-DloggUser="
  private val logg = Log4jLogger.getLogger(mf.runtimeClass)

  def debug(message: String, user: UserEnum, users: UserEnum*) {
    logg(user, users)(() => logg.debug(message))
  }

  def info(message: String, user: UserEnum, users: UserEnum*) {
    logg(user, users)(() => logg.info(message))
  }

  def warn(message: String, user: UserEnum, users: UserEnum*) {
    logg(user, users)(() => logg.warn(message))
  }

  private def logg(user: UserEnum, users: Seq[UserEnum])(loggF: () => Unit) {
    val userList = ListBuffer[UserEnum](user)
    userList ++= users

    if (LoggerObj.loggUser == "default" || userList.contains(All)) {
      loggF()
      return
    }

    if (userList.exists(user => prefix + user.name == LoggerObj.loggUser)) {
      loggF()
    }
  }

  abstract sealed class UserEnum(val name: String)

  case object Igor extends UserEnum("igor")

  case object Vilo extends UserEnum("vilo")

  case object All extends UserEnum("all")


}
