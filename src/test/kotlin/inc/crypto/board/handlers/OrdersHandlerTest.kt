package inc.crypto.board.handlers

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import inc.crypto.board.api.BuyOrder
import inc.crypto.board.api.CoinType
import inc.crypto.board.api.SellOrder
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class OrdersHandlerTest {

    private lateinit var ordersHandler: BoardOrdersHandler
    private lateinit var boardHandler: BoardHandler

    @Before
    fun setUp() {
        boardHandler = mock()
        ordersHandler = BoardOrdersHandler(boardHandler)
    }

    @Test
    fun `placing sell order works correctly`() {
        // given
        val sellOrder = SellOrder("User", CoinType.Ethereum, 20, 5000)

        // when
        ordersHandler.place(sellOrder)

        // then
        verify(boardHandler).sell(sellOrder.pricePennies, sellOrder.coinType, sellOrder.quantity)
    }

    @Test
    fun `placing buy order works correctly`() {
        // given
        val buyOrder = BuyOrder("User", CoinType.Ethereum, 20, 5000)

        // when
        ordersHandler.place(buyOrder)

        // then
        verify(boardHandler).buy(buyOrder.pricePennies, buyOrder.coinType, buyOrder.quantity)
    }

    @Test
    fun `cancelling buy order works correctly`() {
        // given
        val buyOrder = BuyOrder("User", CoinType.Ethereum, 20, 5000)
        val orderId = ordersHandler.place(buyOrder)

        // when
        ordersHandler.cancel(orderId)

        // then
        verify(boardHandler).buy(buyOrder.pricePennies, buyOrder.coinType, -buyOrder.quantity)
    }

    @Test
    fun `cancelling sell order works correctly`() {
        // given
        val sellOrder = SellOrder("User", CoinType.Ethereum, 20, 5000)
        val orderId = ordersHandler.place(sellOrder)

        // when
        ordersHandler.cancel(orderId)

        // then
        verify(boardHandler).sell(sellOrder.pricePennies, sellOrder.coinType, -sellOrder.quantity)
    }

    @Test
    fun `cancelling not existing order throws exception`() {
        assertFailsWith<IllegalArgumentException>("Order with ID=INVALID_ORDER_ID does not exist") {
            ordersHandler.cancel("INVALID_ORDER_ID")
        }
    }
}
