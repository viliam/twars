package sk.kave.tank.actors

import java.util.concurrent.{ThreadFactory, ExecutorService, TimeUnit, AbstractExecutorService}
import java.util.Collections
import com.typesafe
import akka.dispatch.{ExecutorServiceFactory, ExecutorServiceConfigurator, DispatcherPrerequisites}


object JavaFXExecutorService extends AbstractExecutorService {
  def execute(command: Runnable) = javafx.application.Platform.runLater(
    new Runnable() { def run() = command })
  def shutdown(): Unit = ()
  def shutdownNow() = Collections.emptyList[Runnable]
  def isShutdown = false
  def isTerminated = false
  def awaitTermination(l: Long, timeUnit: TimeUnit) = true
}

// Then we create an ExecutorServiceConfigurator so that Akka can use our SwingExecutorService for the dispatchers
class JavaFXEventThreadExecutorServiceConfigurator(config: typesafe.config.Config, prerequisites: DispatcherPrerequisites)
  extends ExecutorServiceConfigurator(config, prerequisites) {

  private val f = new ExecutorServiceFactory {
    def createExecutorService: ExecutorService = JavaFXExecutorService
  }

  def createExecutorServiceFactory(id: String, threadFactory: ThreadFactory) = f
}