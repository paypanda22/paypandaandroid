package app.pay.pandapro.responsemodels.aadhaarOtp

data class AadhaarOtpResponse(
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)