package app.pay.pandapro.responsemodels.transferTo

data class TransferToResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)