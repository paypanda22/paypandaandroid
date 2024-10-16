package app.pay.paypanda.responsemodels.addBankDetails

data class OnboardBankDetailsResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: String?
)