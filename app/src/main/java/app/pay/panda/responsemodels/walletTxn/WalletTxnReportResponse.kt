package app.pay.panda.responsemodels.walletTxn

data class WalletTxnReportResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)