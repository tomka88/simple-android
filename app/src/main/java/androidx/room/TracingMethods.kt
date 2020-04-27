package androidx.room

import com.google.firebase.perf.FirebasePerformance
import java.util.concurrent.Callable

class TracingMethods {

  private fun trace(
      method: String,
      work: Runnable
  ) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "")
    trace.putAttribute("method", method)
    trace.start()
    work.run()
    trace.stop()
  }

  private fun <T> traceWithResult(
      method: String,
      work: Callable<T>
  ): T {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "")
    trace.putAttribute("method", method)
    trace.start()
    val _result = work.call()
    trace.stop()
    return _result
  }
}
