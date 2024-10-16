package app.pay.paypanda.responsemodels.PackageServices


import androidx.annotation.Keep

@Keep
data class PackageServicesResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)