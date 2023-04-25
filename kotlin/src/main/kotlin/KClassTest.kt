interface IRecyclerData {
    fun areItemsTheSame(oldItem: IRecyclerData): Boolean
    fun areContentsTheSame(oldItem: IRecyclerData): Boolean
}

data class FooData(
    val name: String,
    val age: Int,
) : IRecyclerData {
    override fun areItemsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is FooData -> oldItem == this
        else -> false
    }

    override fun areContentsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is FooData -> oldItem == this
        else -> false
    }
}


data class BarData(
    val name: String,
) : IRecyclerData {
    override fun areItemsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is BarData -> oldItem == this
        else -> false
    }

    override fun areContentsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is BarData -> oldItem == this
        else -> false
    }
}


fun main() {
    val foo = FooData("foo", 1)
    val bar = BarData("bar")


}