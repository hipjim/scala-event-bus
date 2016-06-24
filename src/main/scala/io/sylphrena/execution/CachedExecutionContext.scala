package io.sylphrena.execution

import java.util.concurrent._

import scala.concurrent.ExecutionContext

/**
  * 
  * If the thread pool has not reached the core size, it creates new threads.
  * If the core size has been reached and there is no idle threads, it queues tasks.
  * If the core size has been reached, there is no idle threads, and the queue becomes full, it creates new threads (until it reaches the max size).
  * If the max size has been reached, there is no idle threads, and the queue becomes full, the rejection policy kicks in.
  * 
  * Created by dev on 22/06/16.
  */
object CachedExecutionContext {
  val instance = ExecutionContext.fromExecutor(executor)

  private[this] val numProc = Runtime.getRuntime().availableProcessors()

  private[this] val executor = new ThreadPoolExecutor(
      numProc,
      200,
      10 * 60,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue[Runnable]())
  executor.allowCoreThreadTimeOut(true)
}

trait CachedExecutionContext {
  implicit val defaultExecutionContext = CachedExecutionContext.instance
}
