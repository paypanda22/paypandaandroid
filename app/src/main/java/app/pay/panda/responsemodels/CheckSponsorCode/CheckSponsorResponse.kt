package app.pay.panda.responsemodels.CheckSponsorCode

data class CheckSponsorResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: Int?
)