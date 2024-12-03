package app.pay.retailers.responsemodels.forgetPin

data class ForgetPinResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)