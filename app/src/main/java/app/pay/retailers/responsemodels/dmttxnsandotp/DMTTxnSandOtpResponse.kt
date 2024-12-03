package app.pay.retailers.responsemodels.dmttxnsandotp


import androidx.annotation.Keep

@Keep
data class DMTTxnSandOtpResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)