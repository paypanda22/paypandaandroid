package app.pay.paypanda.responsemodels.aepsBe

data class Data(
    val balanceamount: String="",
    val bank_rrn: String="",
    val message: String="",
    val bank_name:String="",
    val lastAadhar:String="",
    val customer_name:String=""
)