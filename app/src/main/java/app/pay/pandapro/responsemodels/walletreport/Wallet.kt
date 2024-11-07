package app.pay.pandapro.responsemodels.walletreport


import androidx.annotation.Keep

@Keep
data class Wallet(
    val __v: Int = 0,
    val _id: String = "",
    val amount: Int = 0,
    val approve: Boolean = false,
    val c_bal: Int = 0,
    val commisionFrom: Any = Any(),
    val createdAt: String = "",
    val message: String = "",
    val o_bal: Int = 0,
    val operator_id: Any = Any(),
    val order_id: String = "",
    val trans_type: String = "",
    val txn_id: String = "",
    val type: String = "",
    val updatedAt: String = "",
    val userid: String = ""
)