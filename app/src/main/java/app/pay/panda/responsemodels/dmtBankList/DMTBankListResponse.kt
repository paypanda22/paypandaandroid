package app.pay.panda.responsemodels.dmtBankList

data class DMTBankListResponse(
    val `data`: List<Data> =listOf(),
    val error: Boolean=false,
    val statusCode: String=""
)