package sk.kave.tank.actors

import sk.kave.tank._
import fx.map.{GameStage, MapGroup}
import sk.kave.tank._
import sk.kave.tank.beans.Game
import sk.kave.tank.fx._
import scalafx.animation.{KeyValue, Timeline, KeyFrame}
import scalafx.Includes._
import scala.Some
import javafx.event.{ActionEvent, EventHandler}
import scalafx.util.Duration
import akka.actor.Actor

/**
 * actor performing moves of map
 *
 * @author Igo & Vil
 */
class MovementActor extends Actor {

  val config = implicitly[Config]

  private val translateX = MapGroup.translateX
  private val translateY = MapGroup.translateY
  private val tankX = MapGroup.tankNode.x
  private val tankY = MapGroup.tankNode.y

  private val tank = Game.tank


  protected var newVect : Vector2D  = (None, None)

  def receive  = {
      case NewDirection(newDirection : Vector2D) =>
        newVect = newDirection
        move
      case m @ AnyRef => logg.debug("MovementActor : Unknow message = " + m)
  }


  private def move() {
    val (h,v) = newVect

    if (MapGroup.canMove(newVect)) {
      val tb = new TimelineBuilder
      tb.addStart( translateX -> translateX() )
      tb.addStart( translateY -> translateY() )

      tb.addEnd (translateX -> (translateX() + getDirectionHorizontal( h)))
      tb.addEnd (translateY -> (translateY() + getDirectionVertical( v)) )

      tb.play(10 ms) {
        val (h,v) = newVect
        MapGroup.move(v)
        MapGroup.move(h)

        tank.move(v)
        tank.move(h)
        sender ! UnLock
      }
    } else if (Game.tank.canMove( newVect)) {
      val tb = new TimelineBuilder
      tb.addStart( tankX -> tankX() )
      tb.addStart( tankY -> tankY() )

      tb.addEnd (tankX -> (tankX() - getDirectionHorizontal( h)))
      tb.addEnd (tankY -> (tankY() - getDirectionVertical( v)) )

      tb.play(10 ms) {
        tank.move(v)
        tank.move(h)
        sender ! UnLock
      }
    } else
      sender ! UnLock
  }


//  private def moveMap() {
//    new Timeline() {
//      onFinished = new EventHandler[ActionEvent] {
//        def handle(e: ActionEvent) {
//          val (h,v) = newVect
//          MapGroup.move(v)
//          MapGroup.move(h)
//
//          sender ! Action.CONTINUE
//        }
//      }
//
//      keyFrames = Seq(
//          at(0 ms) {
//            Set(translateX -> translateX(),
//                translateY -> translateY()
//            )
//          },
//          at(10 ms) {
//            Set(translateX -> (translateX() + getDirectionHorizontal(newVect._1)),
//                translateY -> (translateY() + getDirectionVertical(newVect._2)) )
//          }
//        )
//    }.play
//  }

  class TimelineBuilder() {
    val t = new Timeline()
    val atStart = scala.collection.mutable.HashSet[KeyValue[_,_ <: Object]]()
    val atEnd = scala.collection.mutable.HashSet[KeyValue[_,_ <: Object]]()

    def addStart( k : KeyValue[_,_ <: Object]) = { atStart += k }
    def addEnd( k: KeyValue[_,_ <:Object]) = { atEnd += k}

    def play(time : Duration)( onFinish : => Unit ) {
      new Timeline() {
        onFinished = new EventHandler[ActionEvent] {
          def handle(e: ActionEvent) = onFinish
        }

        keyFrames = Seq(
          at (0 ms)  {atStart.toSet},
          at (time) {atEnd.toSet}
        )
      }.play
    }
  }

  private def getDirectionHorizontal(horizontal: Option[Horizontal]) =
    horizontal match {
      case Some(LEFT) => +config.itemSize
      case Some(RIGHT) => -config.itemSize
      case None => 0
    }

  private def getDirectionVertical(vertical: Option[Vertical]) =
    vertical match {
      case Some(UP) => +config.itemSize
      case Some(DOWN) => -config.itemSize
      case None => 0
    }
}
