class Dependency {
    fun compile(lib: String) {
        println(lib)
    }
}

fun dependencies(foo: Dependency.() -> Unit) {
    val dependency = Dependency()
    foo(dependency)
    //dependency.foo()
    //foo.invoke(dependency)
}

fun dependencies1(foo: (Dependency) -> Unit) {
    val dependency = Dependency()
    foo(dependency)
    //foo.invoke(dependency)
}

fun main() {

    dependencies {
        compile("glide")
        //this.compile("glide")
    }

    dependencies1 {
        it.compile("Retrofit")
    }
}