package io.sylphrena.execution

import java.util.concurrent.ThreadPoolExecutor.AbortPolicy
import java.util.concurrent._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

/**
  * Created by dev on 27/06/16.
  */
trait CancellableFuture[T] {
  def future: Future[T]
  def cancel(mayInterruptIfRunning: Boolean): Boolean
}

trait Scheduler extends ExecutionContext {
  def scheduleOnce[T](p: => T)(delay: FiniteDuration): CancellableFuture[T]

  def scheduleAtFixedRate(delay: FiniteDuration,
                          period: FiniteDuration,
                          runnable: Runnable)
}

object Scheduler {
  private class SchedulerImpl(
      corePoolSize: Int,
      threadFactory: ThreadFactory = Executors.defaultThreadFactory,
      handler: RejectedExecutionHandler = new AbortPolicy)
      extends Scheduler {

    private[this] val underlying: ScheduledExecutorService =
      EventBusExecutionContext.executor

    override def scheduleOnce[T](p: => T)(
        delay: FiniteDuration): CancellableFuture[T] = {
      val promise = Promise[T]()
      val scheduledFuture: ScheduledFuture[_] =
        underlying.schedule(new Runnable {
          override def run() = {
            promise.complete(Try(p))
          }
        }, delay.length, delay.unit)
      new DelegatingCancellableFuture(promise.future, scheduledFuture.cancel)
    }

    override def scheduleAtFixedRate(delay: FiniteDuration,
                                     period: FiniteDuration,
                                     runnable: Runnable): Unit = ???

    override def reportFailure(cause: Throwable): Unit = ???

    override def execute(runnable: Runnable): Unit = underlying.execute(runnable)
  }

  private class DelegatingCancellableFuture[T](val future: Future[T], cancelMethod: (Boolean) ⇒ Boolean) extends CancellableFuture[T] {
    def cancel(interruptIfRunning: Boolean): Boolean =
      cancelMethod(interruptIfRunning)
  }

}
