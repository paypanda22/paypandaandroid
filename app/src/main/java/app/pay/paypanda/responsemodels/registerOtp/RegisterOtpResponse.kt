package app.pay.paypanda.responsemodels.registerOtp

data class RegisterOtpResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)