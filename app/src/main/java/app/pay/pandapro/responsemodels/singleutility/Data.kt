package app.pay.pandapro.responsemodels.singleutility



data class Data (
    val _id: String? ="",
    val txn_id: String="",
    val operator_name: String="",
    val amount: String="",
    val status: String="",
    val ca_num: String="",
    val txnReferenceId: String? ="",
    val createdAt: String="",
    val `user_id`: UserID = UserID(),
    val `category_id`: Categoryid = Categoryid(),
)