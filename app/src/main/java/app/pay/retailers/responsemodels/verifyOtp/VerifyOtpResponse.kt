package app.pay.retailers.responsemodels.verifyOtp

data class VerifyOtpResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)