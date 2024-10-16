package app.pay.paypanda.responsemodels.payoutresponse


import androidx.annotation.Keep

@Keep
data class AepsPayoutResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0,
    val totalCount: Int = 0
)