package app.pay.retailers.responsemodels.requestList


import androidx.annotation.Keep

@Keep
data class WalletRequestListResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)