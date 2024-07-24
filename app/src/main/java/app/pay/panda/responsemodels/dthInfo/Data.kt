package app.pay.panda.responsemodels.dthInfo

data class Data(
    val Address: String? ="",
    val City: String? ="",
    val District: String?  ="",
    val message: String?  ="",
    val monthly: String?  ="",
    val next_recharge_date: String?  ="",
    val pin_code: String?  ="",
    val name:String?=""
)