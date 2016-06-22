package io.sylphrena.events
import java.util.concurrent.CountDownLatch

/**
  * Created by dev on 01/06/16.
  */
object Runner extends App {
  case class Msg(value: String)

  val eb = EventBus().withAsyncExecution
  eb.subscribe[Msg] { t =>
    println(Thread.currentThread().getName + " -> " + t)
  }

  val doneSignal = new CountDownLatch(4)

  for (i <- 0 to 100000000) eb.post {
    Msg(s"xxx -> $i")
  }
}
