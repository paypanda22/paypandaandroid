package app.pay.panda.responsemodels.newForgetPasswordVerify

data class ForgetPasswordVerifyResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)