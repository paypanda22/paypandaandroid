package app.pay.pandapro.responsemodels.addRecipient

data class AddRecipientResponse(
    val `data`: Data=Data(),
    val error: Boolean=false,
    val message: String="",
    val statusCode: Int=0
)