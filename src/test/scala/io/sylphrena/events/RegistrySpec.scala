package io.sylphrena.events

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by dev on 26/06/16.
  */
class RegistrySpec extends FlatSpec with Matchers {

  "A Registry" should "register event handlers" in {
    val registry = new Registry
    registry.registerEventHandler[String](t => t should be("Hello World!"))
    val canHandleStringEvent = registry.hasEventHandlerFor[String]
    canHandleStringEvent shouldBe true
  }


}
