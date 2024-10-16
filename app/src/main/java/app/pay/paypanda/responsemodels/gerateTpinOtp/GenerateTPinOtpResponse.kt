package app.pay.paypanda.responsemodels.gerateTpinOtp

import androidx.annotation.Keep


@Keep
data class GenerateTPinOtpResponse(
    val `data`: Data? = Data(),
    val error: Boolean? = true,
    val message: String? = "",
    val statusCode: Int? = 0
)