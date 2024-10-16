package app.pay.paypanda.responsemodels.pinSendOtp

data class PinSendOtpResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)