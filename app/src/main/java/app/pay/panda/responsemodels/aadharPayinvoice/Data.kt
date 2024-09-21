package app.pay.panda.responsemodels.aadharPayinvoice


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: Any = Any(),
    val amount: Int = 0,
    val bank_name: String = "",
    val createdAt: String = "",
    val mobile_number: String = "",
    val shop_name: String = "",
    val status: String = "",
    val totalAmount: Int = 0,
    val txn_id: String = "",
    val type: String = "",
    val utr: String = ""
)