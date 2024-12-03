package app.pay.retailers.responsemodels.processRecharge

data class Data(
    val close_bal: Double? =0.0,
    val error: Boolean? =true,
    val message: String? ="",
    val open_bal: Double?=0.0,
    val operatorid : String?="",
    val operator_name: String?="",
    val status: Int?=1,
    val txn_id: String?=""
)