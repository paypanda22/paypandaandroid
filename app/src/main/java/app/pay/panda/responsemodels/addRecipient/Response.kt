package app.pay.panda.responsemodels.addRecipient

data class Response(
    val customer_id: String="",
    val message: String="",
    val pipes: List<Any> = listOf(),
    val recipient_id: Int=0,
    val recipient_id_type: String="",
    val recipient_mobile: String="",
    val response_status_id: Int=0,
    val response_type_id: Int=0,
    val status: Int=0
)