package io.sylphrena

/**
  * Created by dev on 23/06/16.
  */
package object events {
  type EventHandler[T] = T => Unit
}
