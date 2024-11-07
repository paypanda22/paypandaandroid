package app.pay.pandapro.responsemodels.walletrequestlistsuper


import androidx.annotation.Keep

@Keep
data class WalletRequestListSuperDist(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)