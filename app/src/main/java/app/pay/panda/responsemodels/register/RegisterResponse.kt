package app.pay.panda.responsemodels.register

data class RegisterResponse(
    val `data`: Data?=Data(),
    val error: Boolean?=true,
    val message: String?="",
    val statusCode: String?=""
)