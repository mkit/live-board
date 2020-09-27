package inc.crypto.board.api

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LiveBoardTest {

    private lateinit var liveBoard: LiveBoard

    @Before
    fun setUp() {
        liveBoard = LiveBoard()
    }

    @Test
    fun `placing sell order puts the entry on the board`() {
        // given
        val sellOrder = SellOrder("User", CoinType.Ethereum, 20, 5000)
        assertEquals(0, liveBoard.board().sell.size)

        // when
        liveBoard.place(sellOrder)

        // then
        assertEquals(1, liveBoard.board().sell.size)
    }

    @Test
    fun `placing buy order puts the entry on the board`() {
        // given
        val buyOrder = BuyOrder("User", CoinType.Ethereum, 20, 5000)
        assertEquals(0, liveBoard.board().buy.size)

        // when
        liveBoard.place(buyOrder)

        // then
        assertEquals(1, liveBoard.board().buy.size)
    }

    @Test
    fun `cancelling buy order removes the entry from the board`() {
        // given
        val buyOrder = BuyOrder("User", CoinType.Ethereum, 20, 5000)
        val orderId = liveBoard.place(buyOrder)

        // when
        liveBoard.cancel(orderId)

        // then
        assertEquals(0, liveBoard.board().buy.size)
    }

    @Test
    fun `cancelling sell order removes the entry from the board`() {
        // given
        val sellOrder = SellOrder("User", CoinType.Ethereum, 20, 5000)
        val orderId = liveBoard.place(sellOrder)

        // when
        liveBoard.cancel(orderId)

        // then
        assertEquals(0, liveBoard.board().sell.size)
    }

    @Test
    fun `the board is live`() {
        // given
        val sellOrder = SellOrder("User", CoinType.Ethereum, 20, 5000)
        val buyOrder = BuyOrder("User", CoinType.Litecoin, 30, 400)
        liveBoard.place(sellOrder)
        liveBoard.place(buyOrder)
        val board = liveBoard.board()
        assertEquals(1, board.sell.size)
        assertEquals(1, board.buy.size)

        // when
        val anotherBuyOrder = BuyOrder("User", CoinType.Litecoin, 50, 400)
        val anotherSellOrder = SellOrder("User", CoinType.Bitcoin, 30, 3000)
        liveBoard.place(anotherBuyOrder)
        liveBoard.place(anotherSellOrder)

        // then
        assertEquals(2, board.sell.size)
        assertEquals(1, board.buy.size)
        assertEquals(80, board.buy.first().quantity)
    }

    @Test
    fun `the board is order asc for sell and desc for buy`() {
        // when
        liveBoard.place(SellOrder("User", CoinType.Ethereum, 20, 1000))
        liveBoard.place(SellOrder("User", CoinType.Ethereum, 30, 5000))
        liveBoard.place(SellOrder("User", CoinType.Ethereum, 70, 3000))
        liveBoard.place(SellOrder("User", CoinType.Ethereum, 340, 2000))
        liveBoard.place(SellOrder("User", CoinType.Litecoin, 15, 1500))


        liveBoard.place(BuyOrder("User", CoinType.Ethereum, 20, 1000))
        liveBoard.place(BuyOrder("User", CoinType.Ethereum, 30, 5000))
        liveBoard.place(BuyOrder("User", CoinType.Ethereum, 70, 3000))
        liveBoard.place(BuyOrder("User", CoinType.Ethereum, 340, 2000))
        liveBoard.place(BuyOrder("User", CoinType.Litecoin, 15, 1500))

        val buy = liveBoard.board().buy
        val sell = liveBoard.board().sell

        // then
        assertEquals(5, sell.size)
        assertEquals(1000, sell.elementAt(0).priceInPennies)
        assertEquals(1500, sell.elementAt(1).priceInPennies)
        assertEquals(2000, sell.elementAt(2).priceInPennies)
        assertEquals(3000, sell.elementAt(3).priceInPennies)
        assertEquals(5000, sell.elementAt(4).priceInPennies)

        assertEquals(5, buy.size)
        assertEquals(5000, buy.elementAt(0).priceInPennies)
        assertEquals(3000, buy.elementAt(1).priceInPennies)
        assertEquals(2000, buy.elementAt(2).priceInPennies)
        assertEquals(1500, buy.elementAt(3).priceInPennies)
        assertEquals(1000, buy.elementAt(4).priceInPennies)

    }
}
