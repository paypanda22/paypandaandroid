package app.pay.panda.responsemodels.walletrequestlistsuper


import androidx.annotation.Keep

@Keep
data class Request(
    val __v: Int = 0,
    val _id: String = "",
    val amount: Int = 0,
    val createdAt: String = "",
    val paymentDate: String = "",
    val remark: String = "",
    val requestTo: RequestTo = RequestTo(),
    val status: String = "",
    val updatedAt: String = "",
    val user_id: String = ""
)