package app.pay.pandapro.responsemodels.gstupdate

data class GstUpdateResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: String?
)