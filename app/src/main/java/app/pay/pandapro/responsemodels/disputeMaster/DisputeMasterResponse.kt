package app.pay.pandapro.responsemodels.disputeMaster

data class DisputeMasterResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)