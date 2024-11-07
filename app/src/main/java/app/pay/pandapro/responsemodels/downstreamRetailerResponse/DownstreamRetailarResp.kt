package app.pay.pandapro.responsemodels.downstreamRetailerResponse


import androidx.annotation.Keep

@Keep
data class DownstreamRetailarResp(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)