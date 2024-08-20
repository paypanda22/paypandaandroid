package app.pay.panda.responsemodels.resendtpin



data class TransactionPinResponse (
    val `data`: Data = Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)