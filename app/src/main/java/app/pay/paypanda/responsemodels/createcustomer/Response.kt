package app.pay.paypanda.responsemodels.createcustomer

data class Response(
    val customer_id: String="",
    val customer_id_type: String="",
    val message: String="",
    val name: String="",
    val otp: String="",
    val otp_ref_id: String="",
    val response_status_id: Int=0,
    val response_type_id: Int=0,
    val status: Int=0
)