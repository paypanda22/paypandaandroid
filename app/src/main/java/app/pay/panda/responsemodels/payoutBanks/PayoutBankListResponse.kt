package app.pay.panda.responsemodels.payoutBanks

data class PayoutBankListResponse(
    val `data`: List<Data>? = listOf(),
    val error: Boolean?=true,
    val statusCode: Int?=0
)