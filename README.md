Scala Event Bus
===========

[![Build Status](https://travis-ci.org/hipjim/scala-event-bus.svg?branch=master)](https://travis-ci.org/hipjim/scala-event-bus)

###### *Simple event bus implementation in Scala.*

```scala
  import io.sylphrena.execution.EventBusExecutionContext._
  import scala.concurrent.duration._

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

  for (i <- 1 to 100)
    eb.post(Msg(i.toString))

  // scheduled event publishing
  eb.postAtInterval[Msg](interval = 10.seconds, initialDelay = 5.seconds) {
    Msg(Source.fromURL("http://api.fixer.io/latest?base=USD").mkString)
  }
```

