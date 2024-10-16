package app.pay.paypanda.responsemodels.payoutresponse


import androidx.annotation.Keep

@Keep
data class Data(
    val __v: Int = 0,
    val _id: String = "",
    val account_name: String = "",
    val ackno: String = "",
    val amount: Int = 0,
    val approval_type: String = "",
    val bank_account_number: String = "",
    val bank_ifsc: String = "",
    val bank_name: String = "",
    val beneid: String = "",
    val charge_amount: Int = 0,
    val createdAt: String = "",
    val is_Charged: Boolean = false,
    val is_refunded: Boolean = false,
    val paymentMode: String = "",
    val status: Int = 0,
    val txnStatus: String = "",
    val txn_id: String = "",
    val updatedAt: String = "",
    val user_id: Any = Any(),
    val user_merchant_code: String = "",
    val user_name: String = ""
)