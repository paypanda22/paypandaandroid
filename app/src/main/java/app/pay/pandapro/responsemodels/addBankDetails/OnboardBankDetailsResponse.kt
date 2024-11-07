package app.pay.pandapro.responsemodels.addBankDetails

data class OnboardBankDetailsResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: String?
)