package app.pay.pandapro.responsemodels.passwordcheck

data class PasswordCheckResponse(
    val `data`: Data= Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)