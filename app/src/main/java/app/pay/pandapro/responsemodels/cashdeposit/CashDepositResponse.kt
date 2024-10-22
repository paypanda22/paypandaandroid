package app.pay.pandapro.responsemodels.cashdeposit

data class CashDepositResponse(
    val `data`: Data?=Data(),
    val error: Boolean?=true,
    val message: String?="N/A",
    val statusCode: Int?=0
)