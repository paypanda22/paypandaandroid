package app.pay.panda.responsemodels.newForgetPassword

data class ForgetPasswordNewResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)