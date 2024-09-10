package app.pay.panda.responsemodels.dmtotp


import androidx.annotation.Keep

@Keep
data class DMTOtpResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: Int = 0
)