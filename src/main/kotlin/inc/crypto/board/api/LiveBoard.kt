package inc.crypto.board.api

import inc.crypto.board.handlers.BoardHandler
import inc.crypto.board.handlers.BoardOrdersHandler
import inc.crypto.board.handlers.OrdersHandler

class LiveBoard : OrdersHandler {

    private val boardHandler = BoardHandler()
    private val ordersHandler = BoardOrdersHandler(boardHandler)

    override fun place(order: Order): String = ordersHandler.place(order)
    override fun cancel(orderId: String) = ordersHandler.cancel(orderId)

    fun board(): Board = Board(boardHandler.sell, boardHandler.buy)
}
