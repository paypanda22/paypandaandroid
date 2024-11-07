package app.pay.pandapro.responsemodels.walletRequest

data class WalletRequestResponse(
    val `data`: Data?,
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)