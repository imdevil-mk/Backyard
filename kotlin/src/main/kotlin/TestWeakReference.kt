import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

class WrapWeakReference<T : Any>(
    obj: T?,
    queue: ReferenceQueue<T>,
) : WeakReference<T>(obj, queue)


class Box()

fun main() = runBlocking {
    var str: Box? = Box()

    val queue: ReferenceQueue<in Box?> = ReferenceQueue()

    val weakReference = WeakReference(str, queue)

    str = null

    System.gc()
    System.runFinalization()

    delay(4000)

    if (weakReference.isEnqueued) {
        println("gc")
    }
    println("fuck")
}