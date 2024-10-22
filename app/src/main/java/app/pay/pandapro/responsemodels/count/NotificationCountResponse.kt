package app.pay.pandapro.responsemodels.count


import androidx.annotation.Keep

@Keep
data class NotificationCountResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)