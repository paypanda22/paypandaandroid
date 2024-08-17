package app.pay.panda.responsemodels.PackageServices


import androidx.annotation.Keep

@Keep
data class PackageServicesResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)