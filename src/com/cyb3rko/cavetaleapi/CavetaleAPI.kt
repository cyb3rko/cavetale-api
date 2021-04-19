package com.cyb3rko.cavetaleapi

import org.jsoup.Jsoup
import java.util.logging.Logger

class CavetaleAPI {
    fun getUser(name: String) = CavetaleUser(name)

    fun getRichlist(limit: Int): List<Pair<String, String>> {
        if (limit < 1 || limit > 100) throw RuntimeException("Limit out of bounds")
        return getListData(
            "https://cavetale.com/market/money.php",
            "body > div.separator > div > table > tbody > tr:nth-child(index)",
            limit
        )
    }

    fun getTopSellers(limit: Int): List<Pair<String, String>> {
        if (limit < 1 || limit > 50) throw RuntimeException("Limit out of bounds")
        return getListData(
            "https://cavetale.com/market/top.php",
            "body > div.separator > div > div > div:nth-child(1) > table > tbody > tr:nth-child(index)",
            limit
        )
    }

    private fun getListData(link: String, itemModifier: String, limit: Int): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        try {
            val doc = Jsoup.connect(link).get()
            repeat(limit) {
                val tableRow = doc.selectFirst(itemModifier.replace("index", "${it + 1}"))
                val name = tableRow.selectFirst("a").text()
                val coins = tableRow.selectFirst(".price.money").text()
                println("$name, $coins")
                list.add(Pair(name, coins))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    fun getAvatarLink(name: String, size: Int) = "https://minotar.net/helm/$name/$size.png"

    fun getBustLink(name: String, size: Int) = "https://minotar.net/armor/bust/$name/$size.png"
}