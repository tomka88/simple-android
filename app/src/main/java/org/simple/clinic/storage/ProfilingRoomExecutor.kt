package org.simple.clinic.storage

import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ProfilingRoomExecutor : Executor {

  private val workExecutor = Executors.newCachedThreadPool()

  override fun execute(command: Runnable) {
    val stacktrace = Throwable()

    stacktrace.fillInStackTrace()

    Timber.tag("db.profile").e(stacktrace)

    val element = stacktrace
        .stackTrace
        .find { it.className.startsWith("org.simple.clinic") && it.className != "org.simple.clinic.storage.ProfilingRoomExecutor" }

    if (element != null) {
      Timber.tag("db.profile").d("Class: ${element.className}.${element.methodName}")
    }

    workExecutor.execute(command)
  }
}
