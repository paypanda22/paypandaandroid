package app.pay.pandapro.responsemodels.asmresponse


import androidx.annotation.Keep

@Keep
data class AsmResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)