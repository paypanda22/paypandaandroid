package app.pay.panda.responsemodels.forgetpassword

data class ForgetPasswordResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)