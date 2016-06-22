package io.sylphrena.events

import java.util.{Timer, TimerTask}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.reflect.ClassTag

import io.sylphrena.events.models.{EventBus, EventHandler}
import io.sylphrena.execution.CachedExecutionContext

object models {
  type EventHandler[T] = T => Unit

  sealed trait EventBus {
    def withAsyncExecution: EventBus
    def post[T](event: => T)(implicit ec: ExecutionContext = CachedExecutionContext.instance): Unit
    def postAtInterval[T](interval: FiniteDuration,
                          initialDelay: FiniteDuration = 0.seconds)(
        event: => T): Unit
    def subscribe[T](e: EventHandler[T])(implicit c: ClassTag[T]): EventBus
  }
}

object EventBus {
  def apply(): EventBus = new EventBusImpl()

  private case class EventBusImpl(async: Boolean = false) extends EventBus {
    private[this] val eventHandlerRegistry = new Registry()
    private[this] val timer = new Timer()

    override def post[T](
        event: => T)(implicit ec: ExecutionContext =
                       CachedExecutionContext.instance): Unit =
      eventHandlerRegistry.lookUpEventHandler(event).foreach { f =>
        if (async) {
          Future(f(event))
        } else {
          f(event)
        }
      }

    override def postAtInterval[T](interval: FiniteDuration,
                                   initialDelay: FiniteDuration = 0.seconds)(
        event: => T): Unit = {
      timer.scheduleAtFixedRate(new TimerTask {
        override def run(): Unit = post(event)
      }, 0, interval.toMillis)
    }

    override def subscribe[T](e: EventHandler[T])(
        implicit c: ClassTag[T]): EventBus = {
      eventHandlerRegistry.registerEventHandler(e)
      this
    }

    override def withAsyncExecution = this.copy(async = true)
  }
}