package app.pay.panda.responsemodels.walletRequest

data class Data(
    val __v: Int=0,
    val _id: String="",
    val account_number: String="",
    val amount: Int=0,
    val bank: String="",
    val bankRef: String="",
    val createdAt: String="",
    val method: String="",
    val paymentDate: String="",
    val remark: String="",
    val status: String="",
    val updatedAt: String="",
    val user_id: String=""
)