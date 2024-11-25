package app.pay.retailers.responsemodels.chkmobile

data class CheckMobileResponse(
    val `data`: Data=Data(),
    val error: Boolean=false,
    val message: String="N/A",
    val statusCode: String="N/A"
)