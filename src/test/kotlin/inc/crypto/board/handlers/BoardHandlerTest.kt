package inc.crypto.board.handlers

import inc.crypto.board.api.CoinType
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class BoardHandlerTest {

    private lateinit var boardHandler: BoardHandler

    @Before
    fun setUp() {
        boardHandler = BoardHandler()
    }

    @Test
    fun `sell board entries are created and they are ordered ascending`() {
        // when
        val prices = arrayOf(2000, 4000, 1000)
        boardHandler.sell(prices[0], CoinType.Bitcoin, 30)
        boardHandler.sell(prices[1], CoinType.Bitcoin, 25)
        boardHandler.sell(prices[2], CoinType.Bitcoin, 20)

        // then
        val sell = boardHandler.sell
        val sorterPrices = prices.sorted()
        sorterPrices.forEachIndexed { index, priceInPennies ->
            assertEquals(priceInPennies, sell.elementAt(index).priceInPennies)
        }
    }

    @Test
    fun `buy board entries are created and they are ordered descending`() {
        // when
        val prices = arrayOf(2000, 4000, 1000)
        boardHandler.buy(prices[0], CoinType.Bitcoin, 30)
        boardHandler.buy(prices[1], CoinType.Bitcoin, 25)
        boardHandler.buy(prices[2], CoinType.Bitcoin, 20)

        // then
        val buy = boardHandler.buy
        val sorterPrices = prices.sorted().reversed()
        sorterPrices.forEachIndexed { index, priceInPennies ->
            assertEquals(priceInPennies, buy.elementAt(index).priceInPennies)
        }
    }

    @Test
    fun `buy board entries quantity is updated correctly`() {
        // when
        boardHandler.buy(2000, CoinType.Bitcoin, 30)
        boardHandler.buy(2000, CoinType.Ethereum, 25)
        boardHandler.buy(2000, CoinType.Bitcoin, 20)

        // then
        val buy = boardHandler.buy
        assertEquals(2, buy.size)
        assertEquals(50, buy.first().quantity)
        assertEquals(25, buy.last().quantity)
    }

    @Test
    fun `sell board entries quantity is updated correctly`() {
        // when
        boardHandler.sell(2000, CoinType.Bitcoin, 30)
        boardHandler.sell(2000, CoinType.Ethereum, 25)
        boardHandler.sell(2000, CoinType.Bitcoin, 20)

        // then
        val sell = boardHandler.sell
        assertEquals(2, sell.size)
        assertEquals(25, sell.first().quantity)
        assertEquals(50, sell.last().quantity)
    }

    @Test
    fun `buy board entries quantity is updated correctly with negative quantity`() {
        // when
        boardHandler.buy(2000, CoinType.Bitcoin, 30)
        boardHandler.buy(2000, CoinType.Ethereum, 25)
        boardHandler.buy(2000, CoinType.Bitcoin, -20)

        // then
        val buy = boardHandler.buy
        assertEquals(2, buy.size)
        assertEquals(10, buy.first().quantity)
        assertEquals(25, buy.last().quantity)
    }

    @Test
    fun `sell board entries quantity is updated correctly with negative quantity`() {
        // when
        boardHandler.sell(2000, CoinType.Bitcoin, 30)
        boardHandler.sell(2000, CoinType.Ethereum, 25)
        boardHandler.sell(2000, CoinType.Bitcoin, -20)

        // then
        val sell = boardHandler.sell
        assertEquals(2, sell.size)
        assertEquals(25, sell.first().quantity)
        assertEquals(10, sell.last().quantity)
    }

    @Test
    fun `buy entry is removed when quantity less than 0`() {
        // when
        boardHandler.buy(2000, CoinType.Bitcoin, 30)
        boardHandler.buy(2000, CoinType.Ethereum, 25)
        boardHandler.buy(2000, CoinType.Bitcoin, -40)

        // then
        val buy = boardHandler.buy
        assertEquals(1, buy.size)
        assertEquals(25, buy.last().quantity)
    }

    @Test
    fun `sell entry is removed when quantity less than 0`() {
        // when
        boardHandler.sell(2000, CoinType.Bitcoin, 30)
        boardHandler.sell(2000, CoinType.Ethereum, 25)
        boardHandler.sell(2000, CoinType.Bitcoin, -40)

        // then
        val sell = boardHandler.sell
        assertEquals(1, sell.size)
        assertEquals(25, sell.first().quantity)
    }

    @Test
    fun `sell board is live`() {
        // when
        boardHandler.sell(1000, CoinType.Bitcoin, 30)
        boardHandler.sell(2000, CoinType.Ethereum, 25)

        // then
        val sell = boardHandler.sell
        assertEquals(2, sell.size)
        assertEquals(30, sell.first().quantity)
        assertEquals(25, sell.last().quantity)

        // when
        boardHandler.sell(3000, CoinType.Bitcoin, 20)

        // then
        assertEquals(3, sell.size)
        assertEquals(30, sell.elementAt(0).quantity)
        assertEquals(25, sell.elementAt(1).quantity)
        assertEquals(20, sell.elementAt(2).quantity)
    }

    @Test
    fun `buy board is live`() {
        // when
        boardHandler.buy(1000, CoinType.Bitcoin, 30)
        boardHandler.buy(2000, CoinType.Ethereum, 25)

        // then
        val buy = boardHandler.buy
        assertEquals(2, buy.size)
        assertEquals(25, buy.first().quantity)
        assertEquals(30, buy.last().quantity)

        // when
        boardHandler.buy(3000, CoinType.Bitcoin, 20)

        // then
        assertEquals(3, buy.size)
        assertEquals(20, buy.elementAt(0).quantity)
        assertEquals(25, buy.elementAt(1).quantity)
        assertEquals(30, buy.elementAt(2).quantity)
    }
}
