package app.pay.retailers.responsemodels.dmttxnlist

data class DmtTransactionListResponse(
    val `data`: Data =Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)