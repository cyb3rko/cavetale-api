import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

class CavetaleAPI {

    fun getUser(name: String) = CavetaleUser(name)

    fun getRichlist(limit: Int): List<Pair<String, String>> {
        if (limit < 1 || limit > 100) throw RuntimeException("Limit out of bounds")
        val list = mutableListOf<Pair<String, String>>()
        runBlocking {
            val doc = Jsoup.connect("https://cavetale.com/market/money.php").get()
            repeat(limit) {
                val tableRow = doc.selectFirst("body > div.separator > div > table > tbody > tr:nth-child(${it + 1})")
                val name = tableRow.selectFirst("a").text()
                val coins = tableRow.selectFirst(".price.money").text()
                println("$name, $coins")
                list.add(Pair(name, coins))
            }
        }
        return list
    }

    fun getTopSellers(limit: Int): List<Pair<String, String>> {
        if (limit < 1 || limit > 50) throw RuntimeException("Limit out of bounds")
        val list = mutableListOf<Pair<String, String>>()
        runBlocking {
            val doc = Jsoup.connect("https://cavetale.com/market/top.php").get()
            repeat(limit) {
                val tableRow = doc.selectFirst("body > div.separator > div > div > div:nth-child(1) > table > tbody > tr:nth-child(${it + 1})")
                val name = tableRow.selectFirst("a").text()
                val coins = tableRow.selectFirst(".price.money").text()
                println("$name, $coins")
                list.add(Pair(name, coins))
            }
        }
        return list
    }
}