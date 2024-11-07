package app.pay.pandapro.responsemodels.zsmresponse


import androidx.annotation.Keep

@Keep
data class ZSMResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)