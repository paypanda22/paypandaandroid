package app.pay.pandapro.responsemodels.customerotp

data class CustomerOtpResponse(
    val `data`: Data?,
    val error: Boolean=false,
    val message: String="",
    val statusCode: Int=0
)