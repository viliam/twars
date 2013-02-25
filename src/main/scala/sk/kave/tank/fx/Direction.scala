package sk.kave.tank.fx

sealed trait Direction

sealed trait Vertical extends Direction

case object DOWN extends Vertical
case object UP extends Vertical

sealed trait Horizontal extends Direction

case object LEFT extends Horizontal
case object RIGHT extends Horizontal
