package app.pay.panda.responsemodels.dmtTransaction

data class Data(
    val account_number: String="",
    val amount: Int=0,
    val batchId: String="",
    val beneficiary_name: String="",
    val currency: String="",
    val customer_id: String="",
    val customer_mobile_number: String="",
    val customer_name: String="",
    val message: String="",
    val recipient_id: String="",
    val response: Int=0,
    val status: Int=0,
    val tid: String="",
    val timestamp: String="",
    val utr: String=""
)