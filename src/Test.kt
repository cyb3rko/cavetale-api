fun main() {
    val api = CavetaleAPI()

    val user = api.getUser("cyb3rko")
    println(user.marketEarnings)

    val list = api.getRichlist(10)
    println(list.size)

    val list2 = api.getTopSellers(20)
    println(list2.size)
}