package app.pay.retailers.responsemodels.aepsBe

data class AepsBeResponse(
    val `data`: Data,
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)