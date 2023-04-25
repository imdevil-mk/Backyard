class FOO

fun <T> foo() {
    val a: T
}

class InBar<in T> {
    fun set(t: T) {

    }
}

interface OutBar<out T> {
    fun get(): T
}

open class Parent
class Child : Parent()

fun main() {
    foo<String>()
}