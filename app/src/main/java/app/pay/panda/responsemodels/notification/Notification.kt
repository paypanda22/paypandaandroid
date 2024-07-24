package app.pay.panda.responsemodels.notification

data class Notification(
    val _id: String="",
    val createdAt: String="",
    val message: String="",
    val readed: Boolean=false,
    val subject: String="",
    val to: String="",
    val type: String="",
    val user_id: String=""
)