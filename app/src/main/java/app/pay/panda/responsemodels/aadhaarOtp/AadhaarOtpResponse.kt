package app.pay.panda.responsemodels.aadhaarOtp

data class AadhaarOtpResponse(
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)