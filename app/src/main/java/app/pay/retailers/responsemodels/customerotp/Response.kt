package app.pay.retailers.responsemodels.customerotp

data class Response(
    val message: String="",
    val otp: String="",
    val otp_ref_id: String="",
    val response_status_id: Int=0
)