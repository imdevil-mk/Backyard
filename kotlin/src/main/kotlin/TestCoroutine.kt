/*import kotlinx.coroutines.*

fun main() = runBlocking {
    println(Thread.currentThread().name)
    launch(Dispatchers.IO) {
        doWorld()
        println("fuck")
    }
    println("Hello,")
}

// 这是你的第一个挂起函数
suspend fun doWorld() {
    println(Thread.currentThread().name)
    delay(1000L)
    println("World!")
}*/


//import kotlinx.coroutines.*
//import kotlin.system.*
//
//fun main() = runBlocking<Unit> {
//    val time = measureTimeMillis {
//        val one = doSomethingUsefulOne()
//        val two = doSomethingUsefulTwo()
//        println("The answer is ${one + two}")
//    }
//    println("Completed in $time ms")
//}
//
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L) // pretend we are doing something useful here
//    return 13
//}
//
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(1000L) // pretend we are doing something useful here, too
//    return 29
//}


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun simple(): Flow<Int> = flow { // flow builder
    for (i in 1..3) {
        println("start to delay, ${Thread.currentThread().name}")
        delay(100) // pretend we are doing something useful here
        emit(i) // emit next value
    }
}


fun main() = runBlocking<Unit> {

    println("1" + Thread.currentThread().name)

    // Launch a concurrent coroutine to check if the main thread is blocked
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k， ${Thread.currentThread().name}")
            delay(100)
        }
    }
    // Collect the flow
    simple().collect { value -> println(value) }

    println("fuck")


    val flow : MutableStateFlow<Int> = MutableStateFlow(-1)

    flow.value = 99

    launch {
        flow.collect{
            println("from MutableStateFlow $it")
        }
    }

    //flow.value = 100

    //flow.value = 101
}