package app.pay.retailers.responsemodels.verifyRegisterOtp

data class VerifyRegisterOtpResponse(
    val `data`: Data?=Data(),
    val error: Boolean?=true,
    val message: String?="",
    val statusCode: Int?=0
)