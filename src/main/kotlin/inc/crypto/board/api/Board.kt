package inc.crypto.board.api

data class Board(val sell: BoardEntryList, val buy: BoardEntryList)

/*
 * I chose to go with integers instead of the floating point types for price and quantity, as this is safer with respect
 * to the library potential usage and rounding issues. This can be easily refactored if not needed.
 */
data class BoardEntry(val priceInPennies: Int, val coinType: CoinType, val quantity: Int)

/*
 * By doing this we prevent the mutability of the board entry list making it read-only.
 */
class BoardEntryList(delegator: Collection<BoardEntry>) : Collection<BoardEntry> by delegator
