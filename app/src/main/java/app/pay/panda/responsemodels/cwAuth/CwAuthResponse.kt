package app.pay.panda.responsemodels.cwAuth

data class CwAuthResponse(
    val `data`: Data= Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)