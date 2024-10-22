package app.pay.pandapro.responsemodels.downstramdistributer


import androidx.annotation.Keep

@Keep
data class DonwStreamDistributerResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)