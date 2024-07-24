package app.pay.panda.responsemodels.serviceStatus

data class CheckServiceStatusResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)