import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class CavetaleUser(name: String) {
    lateinit var elements: Elements
    val balance: String
        get() = elements[0].text()
    val marketEarnings: String
        get() = elements[1].text()
    val marketSpendings: String
        get() = elements[2].text()
    val itemsSold: String
        get() = elements[3].text()
    val itemsBought: String
        get() = elements[4].text()
    val currentOffers: String
        get() = elements[5].text()

    init {
        runBlocking {
            val doc = Jsoup.connect("https://cavetale.com/market/player.php?name=Cyb3rKo").get()
            elements = doc.getElementsByClass("price")
        }
    }

    fun update() {
        runBlocking {
            val doc = Jsoup.connect("https://cavetale.com/market/player.php?name=Cyb3rKo").get()
            elements = doc.getElementsByClass("price")
        }
    }
}