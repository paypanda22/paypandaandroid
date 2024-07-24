package app.pay.panda.responsemodels.verifyBankName

data class VerifyBankResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)