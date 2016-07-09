package io.sylphrena.events

import io.sylphrena.execution.EventBusExecutionContext._

/**
  * Created by dev on 01/06/16.
  */
object Runner extends App {
  final case class Msg(i: Int)

  val eb = EventBus()

  eb.subscribeAsync[Msg] { t =>
    println(Thread.currentThread().getName + " --> 2 : " + t.i)
  }

  for (i <- 1 to 10000000)
    eb.post(Msg(i))
}
