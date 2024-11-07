package app.pay.pandapro.responsemodels.walletreport


import androidx.annotation.Keep

@Keep
data class WalletReportResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalAmount: Int = 0
)