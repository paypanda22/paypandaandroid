package app.pay.pandapro.responsemodels

data class PostApiResponse (
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)