package app.pay.panda.responsemodels.generateotp

data class GenerateOtpResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)