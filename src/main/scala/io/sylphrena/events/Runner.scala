package io.sylphrena.events

import io.sylphrena.execution.EventBusExecutionContext


/**
  * Created by dev on 01/06/16.
  */
object Runner extends App {

  implicit val ec = EventBusExecutionContext.instance

  final case class Msg(i: Int)

  val eb = EventBus().withAsyncExecution

  val subscription = eb.subscribe[Msg] { t =>
    println(Thread.currentThread().getName + " -> " + t)
  }

  for (i <- 0 to 100) eb.post(Msg(i))
}
