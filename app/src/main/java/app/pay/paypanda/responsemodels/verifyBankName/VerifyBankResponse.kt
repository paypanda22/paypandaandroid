package app.pay.paypanda.responsemodels.verifyBankName


import androidx.annotation.Keep

@Keep
data class VerifyBankResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)