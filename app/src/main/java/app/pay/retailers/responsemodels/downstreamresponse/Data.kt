package app.pay.retailers.responsemodels.downstreamresponse

data class Data (
    val _id: String="",
    val name: String="",
    val refer_id: String="",
    val email: String = "",
    val mobile: String = "",
    val user_type: String = "",
    val main_wallet: String="",
    val is_approved: Boolean = false,
    val sponsor: Any? = null
)