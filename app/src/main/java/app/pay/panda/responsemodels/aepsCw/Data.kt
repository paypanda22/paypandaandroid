package app.pay.panda.responsemodels.aepsCw

data class Data(
    val amount: String="",
    val balanceamount: String="",
    val bank_name: String="",
    val bank_rrn: String="",
    val iin: String="",
    val last_aadhar: String="",
    val name: String="",
    val txn_id: String=""
)