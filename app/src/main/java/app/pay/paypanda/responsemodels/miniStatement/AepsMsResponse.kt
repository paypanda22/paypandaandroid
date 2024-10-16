package app.pay.paypanda.responsemodels.miniStatement

data class AepsMsResponse(
    val `data`: Data,
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)