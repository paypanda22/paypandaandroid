package app.pay.panda.responsemodels.adhaarverify

data class AadhaarverifyResponse(
    val error: Boolean?,
    val message: String?,
    val statusCode: String?
)