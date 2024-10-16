package app.pay.paypanda.responsemodels.verifypan

data class VerifyPanResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: String?
)