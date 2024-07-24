package app.pay.panda.responsemodels.utilitytxn

data class Data(
    val _id: String?="",
    val amount: Double?=0.0,
    val biller_id: String?="",
    val close_bal: Double?=0.0,
    val createdAt: String?="",
    val open_bal: Double?=0.0,
    val operator_name: String?="",
    val refId: String?="",
    val status: Int?=0,
    val txnReferenceId: String?="",
    val txn_id: String?="",
    val ca_num: String?="",
)