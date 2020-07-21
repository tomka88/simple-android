package io.reactivex.internal.operators.maybe

import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import timber.log.Timber

class ProfilingMaybeFromCallable<T>(
    maybe: MaybeFromCallable<T>
): Maybe<T>() {

  private val wrapped: MaybeFromCallable<T>

  init {
    val callable = maybe.callable

    wrapped = MaybeFromCallable {
      val now = System.currentTimeMillis()

      val value = callable.call()

      val timeTaken = System.currentTimeMillis() - now

      val stacktrace = Throwable()

      stacktrace.fillInStackTrace()

      Timber.tag("db.profile").e(stacktrace)

      val element = stacktrace
          .stackTrace
          .find { it.className.startsWith("org.simple.clinic") && it.className != "org.simple.clinic.storage.ProfilingRoomExecutor" }

      if (element != null) {
        Timber.tag("db.profile").d("Class: ${element.className}.${element.methodName}")
      }

      Timber.tag("db.profile").d("Time taken: ${timeTaken}ms")

      value
    }
  }

  override fun subscribeActual(observer: MaybeObserver<in T>) {
    wrapped.subscribe(observer)
  }
}
