package app.pay.retailers.responsemodels.packagedetail


import androidx.annotation.Keep


@Keep
data class PackageDetailResponse(
    val `data`:  Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)