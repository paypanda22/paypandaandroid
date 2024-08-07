package app.pay.panda.responsemodels.requestList

data class Usertypeid (
    val _id: String? = null,
    val user_type: String? = null
)

data class RequestTo(
    val _id: String? = null,
    val name: String? = null,
    val mobile: String? = null,
    val user_type_id: Usertypeid? = null
)

data class Request(
    val requestTo: RequestTo? = null,
    val __v: Int = 0,
    val _id: String = "",
    val amount: Long = 0L,
    val createdAt: String = "",
    val paymentDate: String = "",
    val status: String = "",
    val updatedAt: String = "",
    val user_id: String = "",
    val bank: String = "",
    val remark: String = ""
)

data class Data(
    val requestList: List<Request> = listOf(),
    val totalCount: Int = 0
)

data class WalletRequestListResponse(
    val data: Data = Data(),
    val error: Boolean = true,
    val message: String = "",
    val statusCode: String = ""
)
