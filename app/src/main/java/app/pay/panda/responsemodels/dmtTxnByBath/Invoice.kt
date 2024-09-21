package app.pay.panda.responsemodels.dmtTxnByBath

data class Invoice(
    val _id: Any? ="",
    val account_number: String="",
    val allTrans: List<AllTran> = listOf(),
    val bank_name: String="",
    val batchId: String="",
    val beneficiary_name: String="",
    val charge: Int=0,
    val createdAt: String="",
    val customer_mobile: String="",
    val customer_name: String="",
    val mobile_number: String="",
    val tid: String="",
    val shop_name: Any? ="",
    val totalAmount: Int=0
)