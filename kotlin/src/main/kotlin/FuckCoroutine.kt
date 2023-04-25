import jdk.nashorn.internal.objects.Global
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {

    GlobalScope.launch {
        while (true){
            Thread.sleep(10000L)
            println("coroutine")
        }
    }

    var lastDate = System.currentTimeMillis();
    var count = 0
    while (true) {
        val nowDate = System.currentTimeMillis();

        if (nowDate - lastDate > 1000) {
            println(count++)
            lastDate = nowDate
        }
    }
}