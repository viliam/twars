package sk.kave.tank.utils

import org.apache.log4j.Logger

object LoggerObj {
  var loggUser: String = null
}

trait Logger {

  val mf = implicitly[Manifest[this.type]]
  private val prefix = "-DloggUser="
  private val logg = Logger.getLogger(mf.runtimeClass)

  def debug(message: String, users: UserEnum*) {
    logg(users)(() => logg.debug(message))
  }

  def info(message: String, users: UserEnum*) {
    logg(users)(() => logg.info(message))
  }

  def warn(message: String, users: UserEnum*) {
    logg(users)(() => logg.warn(message))
  }

  private def logg(users: Seq[UserEnum])(loggF: () => Unit) {

    if (users == null || users.isEmpty) {
      logg.warn("Logger warning: logg users not defined - no logs will be printed!")
      return
    }

    if (LoggerObj.loggUser == "default" || users.contains(All)) {
      loggF()
      return
    }

    if (users.exists(user => prefix + user.name == LoggerObj.loggUser)) {
      loggF()
    }
  }

  abstract sealed class UserEnum(val name: String)

  case object Igor extends UserEnum("igor")

  case object Vilo extends UserEnum("vilo")

  case object All extends UserEnum("all")


}
