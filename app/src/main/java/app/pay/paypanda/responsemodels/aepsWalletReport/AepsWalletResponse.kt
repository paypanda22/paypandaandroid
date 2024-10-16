package app.pay.paypanda.responsemodels.aepsWalletReport


import androidx.annotation.Keep

@Keep
data class AepsWalletResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: Int = 0
)