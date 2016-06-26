package io.sylphrena.events

import io.sylphrena.execution.EventBusExecutionContext

/**
  * Created by dev on 01/06/16.
  */
object Runner extends App with EventBusExecutionContext {
  final case class Msg(i: Int)

  val eb = EventBus().withAsyncExecution

  eb.subscribe[Msg] { t =>
    println(Thread.currentThread().getName + " -> " + t)
  }

  if (eb.canHandleEventType[Msg])
    for (i <- 0 to 100)
      eb.post(Msg(i))
}
