package app.pay.retailers.responsemodels.packageListResponse

data class PackageListResponse(
    val `data`: List<Data>? = listOf(),
    val error: Boolean?=true,
    val statusCode: String?=""
)