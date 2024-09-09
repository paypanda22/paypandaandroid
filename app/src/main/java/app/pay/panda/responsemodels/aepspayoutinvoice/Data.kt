package app.pay.panda.responsemodels.aepspayoutinvoice


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: Any = Any(),
    val amount: Int = 0,
    val bank_account_number: String = "",
    val bank_name: String = "",
    val createdAt: String = "",
    val customer_name: String = "",
    val mobile_number: String = "",
    val paymentMode: String = "",
    val shop_name: String = "",
    val status: String = "",
    val totalAmount: Int = 0,
    val txn_id: String = "",
    val utr: String = ""
)