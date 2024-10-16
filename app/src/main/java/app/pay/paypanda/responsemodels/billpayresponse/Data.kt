package app.pay.paypanda.responsemodels.billpayresponse

data class Data(
    val close_bal: String="",
    val message: String="",
    val open_bal: String="",
    val operator_id: String="",
    val operator_name: String="",
    val txn_id: String="",
    val ca_number:String="",
    val status:Int=1
)