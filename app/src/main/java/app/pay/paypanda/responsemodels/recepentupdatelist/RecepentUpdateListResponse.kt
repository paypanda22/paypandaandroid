package app.pay.paypanda.responsemodels.recepentupdatelist


import androidx.annotation.Keep

@Keep
data class RecepentUpdateListResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)