package app.pay.pandapro.responsemodels.aepsDailyAuth

data class DailyAuthResponse(
    val `data`: Data,
    val error: Boolean,
    val message: String,
    val statusCode: Int
)