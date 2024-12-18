package app.pay.retailers.responsemodels.serviceStatus


import androidx.annotation.Keep

@Keep
data class CheckServiceStatusResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)