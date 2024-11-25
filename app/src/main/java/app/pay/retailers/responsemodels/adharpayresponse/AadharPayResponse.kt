package app.pay.retailers.responsemodels.adharpayresponse


import androidx.annotation.Keep

@Keep
data class AadharPayResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)