package app.pay.paypanda.responsemodels.aepsTxnList


import androidx.annotation.Keep

@Keep
data class Data(
    val __v: Int = 0,
    val _id: String = "",
    val ack_no: String = "",
    val adhaar_no: String = "",
    val amount: Double = 0.0,
    val api_id: String = "",
    val bal_amount: Double = 0.0,
    val bank_name: String = "",
    val bank_rrn: String = "",
    val closing_balance: Double = 0.0,
    val commission: Double = 0.0,
    val createdAt: String = "",
    val customer_mobile: String = "",
    val distributor_commission: Double = 0.0,
    val distributor_tds: Double = 0.0,
    val dot: String = "",
    val is_commission: Boolean = false,
    val is_iris: String = "",
    val last_adhar: String = "",
    val master_dist_commission: Double = 0.0,
    val master_dist_tds: Double = 0.0,
    val message: String = "",
    val nationalbankidentification: String = "",
    val opening_balance: Double = 0.0,
    val requestremarks: String = "",
    val response: Int = 0,
    val status: Int = 0,
    val txnStatus: String = "",
    val txn_id: String = "",
    val type: String = "",
    val updatedAt: String = "",
    val user: Any = Any(),
    val user_id: String = "",
    val user_member_code: String = "",
    val user_name: String = "",
    val user_tds: Double = 0.0
)