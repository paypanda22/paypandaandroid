package app.pay.pandapro.responsemodels.dmtTransaction

data class DMTMakeTransactionResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)