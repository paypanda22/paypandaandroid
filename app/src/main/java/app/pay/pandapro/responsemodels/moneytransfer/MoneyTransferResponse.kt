package app.pay.pandapro.responsemodels.moneytransfer



data class MoneyTransferResponse (
    val `data`: Data = Data(),
    val error: Boolean=true,
    val status: Int=0,
    val message: String
)