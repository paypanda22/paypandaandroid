package app.pay.panda.responsemodels.newlogin

data class NewLoginResponse(
    val `data`: Data?=Data(),
    val error: Boolean?=true,
    val message: String?="",
    val statusCode: Int?=0
)