package app.pay.pandapro.responsemodels.dmtTxnByBath

data class BatchWiseTxnListResponse(
    val `data`: Data = Data(),
    val error: Boolean=true,
    val message: Boolean=false,
    val statusCode: Int=0
)