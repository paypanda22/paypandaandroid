package app.pay.pandapro.responsemodels.walletTxn

data class Data(
    val total: Int=0,
    val wallet: List<Wallet> = listOf()
)