package app.pay.pandapro.responsemodels.gstverify

data class GstVerificationResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)