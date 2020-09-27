package inc.crypto.board.handlers

import inc.crypto.board.api.BuyOrder
import inc.crypto.board.api.Order
import inc.crypto.board.api.SellOrder
import java.util.*

internal class BoardOrdersHandler(private val boardHandler: BoardHandler) : OrdersHandler {

    private val orders = mutableMapOf<String, Order>()

    @Synchronized
    override fun place(order: Order): String {
        return UUID.randomUUID().toString().also { orderId ->
            orders[orderId] = order
            when (order) {
                is BuyOrder -> boardHandler.buy(order.pricePennies, order.coinType, order.quantity)
                is SellOrder -> boardHandler.sell(order.pricePennies, order.coinType, order.quantity)
            }
        }
    }

    @Synchronized
    override fun cancel(orderId: String) {
        val order = requireNotNull(orders.remove(orderId)) {
            "Order with ID=$orderId does not exist"
        }
        when (order) {
            is BuyOrder -> boardHandler.buy(order.pricePennies, order.coinType, -order.quantity)
            is SellOrder -> boardHandler.sell(order.pricePennies, order.coinType, -order.quantity)
        }
    }
}
