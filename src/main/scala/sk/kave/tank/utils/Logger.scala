package sk.kave.tank.utils

import org.apache.log4j.Logger
import collection.mutable.ListBuffer

object LoggerObj {
  var loggUser: String = null
}

trait Logger {

  val mf = implicitly[Manifest[this.type]]
  private val prefix = "-DloggUser="
  private val logg = Logger.getLogger(mf.runtimeClass)

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
