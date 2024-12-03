package app.pay.retailers.responsemodels.pinChange

data class PasswordChangeResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message:String="",
    val statusCode: String=""
)