class HTML {
    fun body() { }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()  // 创建接收者对象
    init(html)     // 将该接收者对象传给该 lambda
    return html
}

fun main() {
    html {
        body()
    }
}