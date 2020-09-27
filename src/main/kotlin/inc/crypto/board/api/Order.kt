package inc.crypto.board.api

/*
 * Enum types (and an Order class property) could be used as an alternative to sealed classes. However, this way it feels
 * more elegant when it comes to usage in branching.
 */
sealed class Order(
    open val userId: String,
    open val coinType: CoinType,
    open val quantity: Int,
    open val pricePennies: Int
)

data class SellOrder(
    override val userId: String,
    override val coinType: CoinType,
    override val quantity: Int,
    override val pricePennies: Int
) : Order(userId, coinType, quantity, pricePennies)

data class BuyOrder(
    override val userId: String,
    override val coinType: CoinType,
    override val quantity: Int,
    override val pricePennies: Int
) : Order(userId, coinType, quantity, pricePennies)
