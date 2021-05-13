package com.cyb3rko.cavetaleapi

import org.jsoup.Jsoup
import java.util.logging.Logger

class CavetaleAPI {
    fun getUser(name: String) = CavetaleUser(name)

    fun getRichlist(limit: Int): List<Pair<String, String>> {
        if (limit < 1 || limit > 100) throw RuntimeException("Limit out of bounds")
        return getListPlayerData(
            "https://cavetale.com/market/money.php",
            "body > div.separator > div > table > tbody > tr:nth-child(index)",
            limit
        )
    }

    fun getTopSellers(limit: Int): List<Pair<String, String>> {
        if (limit < 1 || limit > 50) throw RuntimeException("Limit out of bounds")
        return getListPlayerData(
            "https://cavetale.com/market/top.php",
            "body > div.separator > div > div > div:nth-child(1) > table > tbody > tr:nth-child(index)",
            limit
        )
    }

    fun getTopItems(limit: Int): List<Triple<String, String, String>> {
        if (limit < 1 || limit > 50) throw RuntimeException("Limit out of bounds")
        return getListItemData(
            "https://cavetale.com/market/top.php",
            "body > div.separator > div > div > div:nth-child(2) > table > tbody > tr:nth-child(index)",
            limit
        )
    }

    private fun getListPlayerData(link: String, itemModifier: String, limit: Int): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        try {
            val doc = Jsoup.connect(link).get()
            repeat(limit) {
                val tableRow = doc.selectFirst(itemModifier.replace("index", "${it + 1}"))
                val name = tableRow.selectFirst("a").text()
                val coins = tableRow.selectFirst(".price.money").text()
                list.add(Pair(name, coins))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    private fun getListItemData(link: String, itemModifier: String, limit: Int): List<Triple<String, String, String>> {
        val list = mutableListOf<Triple<String, String, String>>()
        try {
            val doc = Jsoup.connect(link).get()
            repeat(limit) {
                val tableRow = doc.selectFirst(itemModifier.replace("index", "${it + 1}"))
                val moneyElements = tableRow.select(".price.money")
                val item = tableRow.selectFirst("a").text()
                val amount = moneyElements[0].text()
                val turnover = moneyElements[1].text()
                list.add(Triple(item, amount, turnover))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    fun getMarketResults(htmlSource: String): List<List<String>> {
        val list = mutableListOf<List<String>>()
        try {
            val doc = Jsoup.parse(htmlSource)
            val elements = doc.select("#result > tbody > *")
            elements.forEach {
                val moneyElements = it.select(".price").eachText().toTypedArray()
                val seller = it.select(".playername").text()
                val sellItem = it.select(".offer").text()
                val amount = it.select(".amount").text()
                list.add(listOf(seller, sellItem, amount, *moneyElements))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    fun getItemsSold(htmlSource: String): List<List<String>> {
        val list = mutableListOf<List<String>>()
        try {
            val doc = Jsoup.parse(htmlSource)
            val elements = doc.select("#result > tbody > *")
            elements.forEach {
                val sellItem = it.select(".offer").text()
                val amount = it.select(".amount").text()
                val price = it.select(".price").text()
                val buyer = it.select(".playername")[1].child(0).text()
                list.add(listOf(sellItem, amount, price, buyer))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    fun getItemsBought(htmlSource: String): List<List<String>> {
        val list = mutableListOf<List<String>>()
        try {
            val doc = Jsoup.parse(htmlSource)
            val elements = doc.select("#result > tbody > *")
            elements.forEach {
                val buyItem = it.select(".offer").text()
                val amount = it.select(".amount").text()
                val price = it.select(".price").text()
                val seller = it.select(".playername")[1].child(0).text()
                list.add(listOf(buyItem, amount, price, seller))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    fun getCurrentOffers(htmlSource: String): List<List<String>> {
        val list = mutableListOf<List<String>>()
        try {
            val doc = Jsoup.parse(htmlSource)
            val elements = doc.select("#result > tbody > *")
            elements.forEach {
                val sellItem = it.select(".offer").text()
                val amount = it.select(".amount").text()
                val moneyElements = it.select(".price").eachText().toTypedArray()
                list.add(listOf(sellItem, amount, *moneyElements))
            }
        } catch (e: Exception) {
            val log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
            log.severe("Failed website parsing with exception: ${e.message}")
        }
        return list
    }

    fun getSearchPhrase(item: String, player: String = "", sell: Boolean = false): String {
        val processedItem = item.replace(" ", "+")
        val sellSuffix = if (sell) "&type=sell" else ""
        return "https://cavetale.com/market/?q=$processedItem&p=$player$sellSuffix"
    }

    fun getItemsSoldLink(name: String) = "https://cavetale.com/market/player.php?name=$name&q=sold"

    fun getItemsBoughtLink(name: String) = "https://cavetale.com/market/player.php?name=$name&q=bought"

    fun getCurrentOffersLink(name: String) = "https://cavetale.com/market/player.php?name=$name&q=offers"

    fun getAvatarLink(name: String, size: Int) = "https://minotar.net/helm/$name/$size.png"

    fun getBustLink(name: String, size: Int) = "https://minotar.net/armor/bust/$name/$size.png"
}