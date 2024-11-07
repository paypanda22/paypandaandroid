package app.pay.pandapro.responsemodels.aepsBe

data class AepsBeResponse(
    val `data`: Data,
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)