package app.pay.retailers.responsemodels.aepsTxnList


import androidx.annotation.Keep

@Keep
data class AepsTransactionsResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalAmount: Double = 0.0,
    val totalCount: Int = 0
)