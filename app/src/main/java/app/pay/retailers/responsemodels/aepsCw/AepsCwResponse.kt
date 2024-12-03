package app.pay.retailers.responsemodels.aepsCw

data class AepsCwResponse(
    val `data`: Data,
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)