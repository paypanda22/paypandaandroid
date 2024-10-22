package app.pay.pandapro.responsemodels.requestList


import androidx.annotation.Keep

@Keep
data class WalletRequestListResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)