package app.pay.pandapro.responsemodels.pinChange

data class PasswordChangeResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message:String="",
    val statusCode: String=""
)