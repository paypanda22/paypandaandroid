package app.pay.panda.responsemodels.dmtstatus


import androidx.annotation.Keep

@Keep
data class DmtDisputePriority(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: String = ""
)