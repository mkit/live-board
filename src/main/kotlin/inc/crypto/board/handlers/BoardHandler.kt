package inc.crypto.board.handlers

import inc.crypto.board.api.BoardEntry
import inc.crypto.board.api.BoardEntryList
import inc.crypto.board.api.CoinType
import java.util.*

internal class BoardHandler {

    /*
     * TreeMap is chosen here due to its performance O(log(n)). A concurrency-safe data structures provided by Java
     * could be used instead but they would not bring much of the benefit in this particular scenario.
     */
    private val internalSell = TreeMap<BoardEntryKey, BoardEntry>(
        Comparator.comparingInt(BoardEntryKey::priceInPennies).thenComparingInt { it.coinType.ordinal })

    /*
     * We reverse the order inside the comparator maintaining the natural ordering of the TreeMap which is more performant.
     */
    private val internalBuy = TreeMap<BoardEntryKey, BoardEntry>(
        Comparator.comparingInt(BoardEntryKey::priceInPennies).thenComparingInt { it.coinType.ordinal }.reversed()
    )

    val sell = BoardEntryList(internalSell.values)
    val buy = BoardEntryList(internalBuy.values)

    /*
     * It would be nice to add change notification.
     * I skipped this in order to avoid over engineering and not sticking to the requirements.
     */

    /**
     * Updates the sell part of the board with the given parameters.
     */
    @Synchronized
    fun sell(priceInPennies: Int, coinType: CoinType, quantity: Int) =
        internalSell.update(priceInPennies, coinType, quantity)

    /**
     * Updates the buy part of the board with the given parameters.
     */
    @Synchronized
    fun buy(priceInPennies: Int, coinType: CoinType, quantity: Int) =
        internalBuy.update(priceInPennies, coinType, quantity)

    private fun TreeMap<BoardEntryKey, BoardEntry>.update(priceInPennies: Int, coinType: CoinType, quantity: Int) {
        compute(
            BoardEntryKey(
                priceInPennies,
                coinType
            )
        ) { _, currentValue ->
            val newEntry = currentValue?.let { entry ->
                BoardEntry(priceInPennies, coinType, quantity + entry.quantity)
            } ?: BoardEntry(priceInPennies, coinType, quantity)
            if (newEntry.quantity > 0) newEntry else null
        }
    }

    private data class BoardEntryKey(val priceInPennies: Int, val coinType: CoinType)
}


