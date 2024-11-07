package app.pay.pandapro.responsemodels.newForgetPassword

data class ForgetPasswordNewResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)