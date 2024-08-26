package app.pay.panda.responsemodels.requestList


import androidx.annotation.Keep

@Keep
data class Request(
    val __v: Int = 0,
    val _id: String = "",
    val account_number: String = "",
    val amount: Int = 0,
    val approved_by: String = "",
    val bank: String = "",
    val bankRef: String = "",
    val createdAt: String = "",
    val method: String = "",
    val paymentDate: String = "",
    val receipt_img: String = "",
    val remark: String = "",
    val remarkByAdmin: String = "",
    val status: String = "",
    val updatedAt: String = "",
    val user_id: String = ""
)