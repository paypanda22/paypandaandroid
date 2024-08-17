package app.pay.panda.responsemodels.payoutTxn


import androidx.annotation.Keep

@Keep
data class PayoutTransactionResponse(
    val `data`: Data = Data(),
    val error: Boolean = true,
    val message: String = "",
    val statusCode: String = ""
)