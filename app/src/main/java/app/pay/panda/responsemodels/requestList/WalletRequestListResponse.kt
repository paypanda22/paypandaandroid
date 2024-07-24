package app.pay.panda.responsemodels.requestList

data class WalletRequestListResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)