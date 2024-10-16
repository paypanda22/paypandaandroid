package app.pay.paypanda.responsemodels.serviceStatus

data class CheckServiceStatusResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String="",
    val message: String=""
)