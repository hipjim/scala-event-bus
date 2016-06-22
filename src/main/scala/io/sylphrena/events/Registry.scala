package io.sylphrena.events

import java.lang.reflect.{ParameterizedType, Type}

import io.sylphrena.events.models.EventHandler

import scala.collection.mutable
import scala.language.higherKinds
import scala.reflect.ClassTag

/**
  * Registry that is mapping events to event listeners.
  */
private[this] final class Registry {
  private[this] val eventHandlers =
    mutable.Map[Class[_], List[EventHandler[_]]]()

  def registerEventHandler[T: ClassTag](h: EventHandler[T]): Unit = {
    val key = implicitly[ClassTag[T]].runtimeClass
    val l: List[EventHandler[_]] = eventHandlers.getOrElse(key, Nil)
    val x = l :+ h
    eventHandlers.put(key, x)
  }

  def lookUpEventHandler[T](e: T): List[EventHandler[T]] =
    doLookup[T, EventHandler](e.getClass).reverse

  private def doLookup[T, W[_]](cls: Class[_]): List[W[T]] = {
    val clazz = supertypes(cls)
    clazz.flatMap { x =>
      eventHandlers.getOrElse(x, Nil).asInstanceOf[List[W[T]]].reverse
    }
  }

  private def supertypes(cls: Class[_]): List[Class[_]] = {
    val parents = Option(cls.getSuperclass).toList ++ cls.getInterfaces.toList
    cls :: parents.flatMap(supertypes)
  }
}
