package io.sylphrena.events

import io.sylphrena.execution.EventBusExecutionContext._

import scala.concurrent.duration._
import scala.io.Source

/**
  * Created by dev on 01/06/16.
  */
object Runner extends App {
  final case class Msg(content: String)

  val eb = EventBus()

  eb.subscribe[Msg] { t =>
    println(t.content)
  }

  // subscribing an async message handler.
  // it will be executed as a Future on the event bus execution context
  eb.subscribeAsync[Msg] { t =>
    println(s"${Thread.currentThread().getName} --> ${t.content}")
  }

//  for (i <- 1 to 10000000)
//    eb.post(Msg(i))

  eb.postAtInterval[Msg](interval = 10.seconds, initialDelay = 5.seconds) {
    Msg(Source.fromURL("http://api.fixer.io/latest?base=USD").mkString)
  }
}
