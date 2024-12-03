package app.pay.retailers.responsemodels.pincode


import androidx.annotation.Keep

@Keep
data class PinCodeResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: Int = 0
)