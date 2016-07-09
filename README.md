# scala-event-bus
Simple scala event bus

```scala
  import io.sylphrena.execution.EventBusExecutionContext._
  import scala.concurrent.duration._

  val eb = EventBus()

  eb.subscribe[Msg] { t =>
    println(t.content)
  }

  // subscribing an async message handler.
  // it will be executed as a Future on the event bus execution context
  eb.subscribeAsync[Msg] { t =>
    println(Thread.currentThread().getName + " --> " + t.content)
  }

  for (i <- 1 to 100)
    eb.post(Msg(i.toString))

  eb.postAtInterval[Msg](interval = 10.seconds, initialDelay = 5.seconds) {
      Msg(Source.fromURL("http://api.fixer.io/latest?base=USD").mkString)
   }
```

