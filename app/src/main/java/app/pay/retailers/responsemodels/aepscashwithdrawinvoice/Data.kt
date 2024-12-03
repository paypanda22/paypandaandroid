package app.pay.retailers.responsemodels.aepscashwithdrawinvoice


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: Any = Any(),
    val adhaar_number: String = "",
    val amount: String = "",
    val bank_name: String = "",
    val createdAt: String = "",
    val customer_mobile: String = "",
    val mobile_number: String = "",
    val response: String = "",
    val shop_name: String = "",
    val totalAmount: String = "",
    val txn_id: String = "",
    val type: String = "",
    val utr: String = ""
)