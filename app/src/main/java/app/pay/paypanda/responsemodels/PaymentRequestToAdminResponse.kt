package app.pay.paypanda.responsemodels

data class PaymentRequestToAdminResponse(
    val error: Boolean,
    val statusCode: String,
    val message: String,
    val data: Data
)

data class Data(
    val requestList: List<Request>,
    val totalCount: Int
)

data class Request(
    val _id: String,
    val amount: Int,
    val status: String,
    val remark: String,
    val createdAt: String,
    val user_id: String? = null,
    val user_mobile: String? = null,
    val user_type: String? = null
)
