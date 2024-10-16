package app.pay.paypanda.responsemodels.utilitytxn

data class UtilityTxnResponse(
    val data: List<Data>? = listOf(),
    val error: Boolean? =true,
    val message: String?="",
    val statusCode: Int?=0,
    val totalAmount: Double?=0.0,
    val totalCount: Int?=0
)