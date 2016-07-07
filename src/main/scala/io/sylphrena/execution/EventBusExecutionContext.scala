package io.sylphrena.execution

import java.util.concurrent._
import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.ExecutionContext

/**
  * 
  * If the thread pool has not reached the core size, it creates new threads.
  * If the core size has been reached and there is no idle threads, it queues tasks.
  * If the core size has been reached, there is no idle threads,
  * and the queue becomes full, it creates new threads (until it reaches the max size).
  * If the max size has been reached, there is no idle threads,
  * and the queue becomes full, the rejection policy kicks in.
  * 
  * Created by dev on 22/06/16.
  */
object EventBusExecutionContext {
  private[this] val numProc = Runtime.getRuntime.availableProcessors()

  private case class DaemonThreadsFactory(name: String) extends ThreadFactory {
    private[this] val threadNumber = new AtomicInteger(1)

    def newThread(r: Runnable): Thread = {
      val thread = Executors.defaultThreadFactory().newThread(r)
      val threadName = name + "-thread-" + threadNumber.getAndIncrement
      thread.setName(threadName)
      thread
    }
  }

  val executor = new ScheduledThreadPoolExecutor(numProc, DaemonThreadsFactory("event-bus"))
  val instance = ExecutionContext.fromExecutor(executor)

  implicit val defaultExecutionContext = EventBusExecutionContext.instance
}
