package app.pay.pandapro.responsemodels.cashdeposit

data class Data(
    val amount: Int?=0,
    val bal_amount: Double?=0.0,
    val bank_name: String?="",
    val bank_rrn: String?="",
    val errorCode: Boolean?=false,
    val iin: String?="",
    val last_adhaar: String?="",
    val message: String?="",
    val status: Int?=0
)