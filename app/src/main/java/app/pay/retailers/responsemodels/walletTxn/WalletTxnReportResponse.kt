package app.pay.retailers.responsemodels.walletTxn

data class WalletTxnReportResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)