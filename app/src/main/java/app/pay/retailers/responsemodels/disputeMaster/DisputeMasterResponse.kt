package app.pay.retailers.responsemodels.disputeMaster

data class DisputeMasterResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)