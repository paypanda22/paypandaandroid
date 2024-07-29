package app.pay.panda.responsemodels.dmtTxnEnq

data class DmtTxnEnqResponse(
    val `data`: Data= Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0,
    val totalCount: Int=0
)