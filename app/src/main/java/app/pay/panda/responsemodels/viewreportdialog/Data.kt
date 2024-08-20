package app.pay.panda.responsemodels.viewreportdialog

data class Data (
    val _id: String="",
    val amount: String="",
    val txn_id: String="",
    val trans_type: String="",
    val createdAt: String="",
    val opening_balance: String="",
    val closing_balance: String="",
)