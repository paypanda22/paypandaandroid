package app.pay.panda.responsemodels.miniStatement

data class Ministatement(
    val amount: String="0",
    val date: String="",
    val narration: String="",
    val txnType: String=""
)