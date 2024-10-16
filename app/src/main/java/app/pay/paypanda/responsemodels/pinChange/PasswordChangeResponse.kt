package app.pay.paypanda.responsemodels.pinChange

data class PasswordChangeResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message:String="",
    val statusCode: String=""
)