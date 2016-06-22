package io.sylphrena.events

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.reflect.ClassTag

import io.sylphrena.events.models.{EventBus, EventHandler}

object models {
  type EventHandler[T] = T => Unit

  sealed trait EventBus {
    def withAsyncExecution: EventBus
    def post[T](event: T): Unit
    def subscribe[T](e: EventHandler[T])(implicit c: ClassTag[T]): EventBus
  }
}

object EventBus {
  def apply(): EventBus = new EventBusImpl()

  private case class EventBusImpl(async: Boolean = false) extends EventBus {
    private[this] val eventHandlerRegistry = new Registry()

    override def post[T](event: T): Unit =
      eventHandlerRegistry.lookUpEventHandler(event).foreach { f =>
        if (async) {
          Future(f(event))
        } else {
          f(event)
        }
      }

    override def subscribe[T](e: EventHandler[T])(
        implicit c: ClassTag[T]): EventBus = {
      eventHandlerRegistry.registerEventHandler(e)
      this
    }

    override def withAsyncExecution = this.copy(async = true)
  }
}
