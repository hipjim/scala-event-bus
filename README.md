# scala-event-bus
Simple scala event bus

```scala
  val eb = EventBus().withAsyncExecution

  eb.subscribe[Msg] { t =>
    println(t)
  }

  if (eb.canHandleEventType[Msg])
    for (i <- 0 to 100)
      eb.post(Msg(i))
```