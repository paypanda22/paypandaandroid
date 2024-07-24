package app.pay.panda.responsemodels.walletTxn

data class Wallet(
    val __v: Int=0,
    val _id: String="",
    val amount: Double=0.00,
    val approve: Boolean=false,
    val c_bal: Double=0.00,
    val createdAt: String="",
    val message: String="",
    val o_bal: Double=0.00,
    val operator_id: String="",
    val order_id: String="",
    val trans_type: String="",
    val txn_id: String="",
    val type: String="",
    val updatedAt: String="",
    val userid: String=""
)