package app.pay.retailers.responsemodels.cashDepositBanks

data class CashDepositBankResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean?=true,
    val statusCode: Int?=0
)