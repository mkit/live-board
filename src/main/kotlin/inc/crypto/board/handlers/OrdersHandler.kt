package inc.crypto.board.handlers

import inc.crypto.board.api.Order

interface OrdersHandler {

    /**
     * Places the given order and returns a newly assigned identifier.
     */
    fun place(order: Order): String

    /**
     * Cancels the order with the given identifier.
     *
     * @throws IllegalAccessException if the order with the request identifier does not exist.
     */
    fun cancel(orderId: String)
}
