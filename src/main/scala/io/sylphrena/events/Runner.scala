package io.sylphrena.events

/**
  * Created by dev on 01/06/16.
  */
object Runner extends App {

  val eb = EventBus().withAsyncExecution
  eb.subscribe[String](t => println(t))
  eb.subscribe[String](t => println("- " + t))
  eb.subscribe[String](t => println("- - " + t))
  eb.subscribe[String](t => println("- - -" + t))
  eb.subscribe[String](t => println("- - - - " + t))
  eb.subscribe[String](t => println("- - - - -" + t))
  eb.subscribe[String](t => println("- - - - - -" + t))
  eb.subscribe[String](t => println("- - - - -" + t))
  eb.subscribe[String](t => println("- - - -" + t))
  eb.subscribe[String](t => println("- - -" + t))
  eb.subscribe[String](t => println("- -" + t))
  eb.subscribe[String](t => println("-" + t))
  eb.subscribe[String](t => println(t))

  eb.post("Hello")
  Thread.sleep(10000)
}
