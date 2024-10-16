package app.pay.paypanda.responsemodels.notification

data class Data(
    val count: Int=0,
    val notifications: List<Notification> = listOf()
)