package app.pay.retailers.responsemodels.verifyRecipientbenificary


import androidx.annotation.Keep

@Keep
data class VerifyRecipientOTPResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)