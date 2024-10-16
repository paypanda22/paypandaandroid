package app.pay.paypanda.responsemodels.aepsDailyAuth

data class DailyAuthResponse(
    val `data`: Data,
    val error: Boolean,
    val message: String,
    val statusCode: Int
)