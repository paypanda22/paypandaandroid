package app.pay.retailers.responsemodels.aepsDailyAuth

data class DailyAuthResponse(
    val `data`: Data,
    val error: Boolean,
    val message: String,
    val statusCode: Int
)