package app.pay.pandapro.responsemodels.downstreamresponse

data class Data (
    val _id: String="",
    val name: String="",
    val refer_id: String="",
    val is_approved: String="",
    val main_wallet: String="",
    val sponsor: List<Any> = listOf()
)