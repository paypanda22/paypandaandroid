package app.pay.panda.responsemodels.reqestreg

data class ReqRegistrationResponse(
    val error: Boolean?=true,
    val message: String?="",
    val statusCode: Int?=0
)