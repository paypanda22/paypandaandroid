package app.pay.retailers.responsemodels.gstupdate

data class GstUpdateResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: String?
)