package app.pay.panda.responsemodels.aepsTxnList

data class Data(
    val __v: Int=0,
    val _id: String="",
    val ack_no: String="",
    val amount: Int=0,
    val bal_amount: Double=0.0,
    val bank_name: String="",
    val bank_rrn: String="",
    val closing_balance: Double=0.0,
    val createdAt: String="",
    val customer_mobile: String="",
    val is_commission: Boolean=false,
    val last_adhar: String="",
    val nationalbankidentification: String="",
    val opening_balance: Double=0.0,
    val response: Int=1,
    val type: String="",
    val updatedAt: String="",
    val user_id: String="",
    val txn_id:String=""
)