package app.pay.paypanda.responsemodels.aepsWalletReport


import androidx.annotation.Keep

@Keep
data class Data(
    val total: Int = 0,
    val wallet: List<Wallet> = listOf()
)