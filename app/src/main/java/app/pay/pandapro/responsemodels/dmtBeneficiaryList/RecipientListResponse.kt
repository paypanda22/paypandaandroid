package app.pay.pandapro.responsemodels.dmtBeneficiaryList

data class RecipientListResponse(
    val `data`: List<Data> =listOf(),
    val error: Boolean=false,
    val message: String="",
    val statusCode: Int=0
)