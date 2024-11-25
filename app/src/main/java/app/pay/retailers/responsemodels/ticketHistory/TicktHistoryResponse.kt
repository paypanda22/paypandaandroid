package app.pay.retailers.responsemodels.ticketHistory


import androidx.annotation.Keep

@Keep
data class TicktHistoryResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: String = "",
    val totalCount: Int = 0
)