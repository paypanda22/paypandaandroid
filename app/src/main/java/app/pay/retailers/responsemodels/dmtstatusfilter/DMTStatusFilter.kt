package app.pay.retailers.responsemodels.dmtstatusfilter


import androidx.annotation.Keep

@Keep
data class DMTStatusFilter(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: String = ""
)