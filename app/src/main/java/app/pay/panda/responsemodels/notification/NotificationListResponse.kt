package app.pay.panda.responsemodels.notification

data class NotificationListResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)