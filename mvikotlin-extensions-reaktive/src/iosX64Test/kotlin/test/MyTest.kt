package test

import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.scheduler.submit
import com.badoo.reaktive.utils.atomic.AtomicBoolean
import com.badoo.reaktive.utils.clock.DefaultClock
import com.badoo.reaktive.utils.lock.Condition
import com.badoo.reaktive.utils.lock.Lock
import com.badoo.reaktive.utils.lock.synchronized
import kotlin.test.Test
import kotlin.test.assertTrue

class MyTest {

    @Test
    fun foo() {
        println("Testing foo")

        val isOk = AtomicBoolean()
        val lock = Lock()
        val condition = lock.newCondition()

        mainScheduler.submit {
            lock.synchronized {
                isOk.value = true
                condition.signal()
            }
        }

        lock.synchronized {
            assertTrue(condition.waitFor(5_000_000_000L, isOk::value), "Error waiting for isOK")
        }

        println("Ok")
    }
}

inline fun Condition.waitFor(timeoutNanos: Long, predicate: () -> Boolean): Boolean {
    require(timeoutNanos >= 0L) { "Timeout must be not be negative" }

    val endNanos = DefaultClock.uptimeNanos + timeoutNanos
    var remainingNanos = timeoutNanos

    while (true) {
        if (predicate()) {
            return true
        }
        if (remainingNanos <= 0L) {
            return false
        }

        await(remainingNanos)
        remainingNanos = endNanos - DefaultClock.uptimeNanos
    }
}
