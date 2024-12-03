package app.pay.retailers.responsemodels.aepsWalletReport


import androidx.annotation.Keep

@Keep
data class Wallet(
    val __v: Int = 0,
    val _id: String = "",
    val amount: Double = 0.0,
    val approve: Boolean = false,
    val c_bal: Double = 0.0,
    val commisionFrom: String = "",
    val createdAt: String = "",
    val is_by_admin: Boolean = false,
    val message: String = "",
    val o_bal: Double = 0.0,
    val operator_id: Any = Any(),
    val order_id: String = "",
    val txn_id: String = "",
    val type: String = "",
    val updatedAt: String = "",
    val userid: String = ""
)