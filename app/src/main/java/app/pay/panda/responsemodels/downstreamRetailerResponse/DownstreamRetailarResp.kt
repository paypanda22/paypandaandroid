package app.pay.panda.responsemodels.downstreamRetailerResponse


import androidx.annotation.Keep

@Keep
data class DownstreamRetailarResp(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)