package app.pay.retailers.responsemodels.bankverification


import androidx.annotation.Keep

@Keep
data class BankVerifictionResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)