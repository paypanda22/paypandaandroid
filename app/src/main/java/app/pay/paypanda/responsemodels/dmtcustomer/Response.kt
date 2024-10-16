package app.pay.paypanda.responsemodels.dmtcustomer

data class Response(
    val available_limit: Int=0,
    val bc_available_limit: Int=0,
    val currency: String="",
    val customer_id: String="",
    val customer_id_type: String="",
    val invalid_params_message: String="",
    val message: String="",
    val mobile: String="",
    val name: String="",
    val response_status_id: Int=0,
    val response_type_id: Int=0,
    val state_desc: String="",
    val status: Int=0,
    val total_limit: Int=0,
    val used_limit: Int=0,
    val user_code: String=""
)