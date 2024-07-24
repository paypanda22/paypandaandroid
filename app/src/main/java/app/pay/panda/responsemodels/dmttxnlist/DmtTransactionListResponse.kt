package app.pay.panda.responsemodels.dmttxnlist

data class DmtTransactionListResponse(
    val `data`: Data =Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)