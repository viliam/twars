package sk.kave.tank.actors

import actors.{SchedulerAdapter, Actor}

trait JfxActor extends Actor {

  override val scheduler = new SchedulerAdapter {
    def execute( codeBlock : => Unit) : Unit = {
      javafx.application.Platform.runLater(new Runnable() {
        def run() = codeBlock
      })
    }
  }
}
