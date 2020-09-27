package inc.crypto.board.api

/*
 * I introduce this type here for simplicity. This is however a big constrain as whenever we want to add another crypto
 * support, we would need to recompile the library. A better approach would be to use just a string or an integer
 * for encoding the currency type. This way, adding a new currency support would be seamless.
 */
enum class CoinType {
    Litecoin, Ethereum, Bitcoin
}
