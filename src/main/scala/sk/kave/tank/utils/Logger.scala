package sk.kave.tank.utils

import org.apache.log4j.Logger

trait Logger {

  val mf = implicitly[ Manifest[this.type] ]

  val logg = Logger.getLogger( mf.runtimeClass)
}
