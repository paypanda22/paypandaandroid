package app.pay.pandapro.responsemodels.miniStatement

data class Data(
    val balanceamount: Double=0.0,
    val message: String="",
    val bank_rrn:String="",
    val bank_name:String="",
    val lastAadhar:String="",
    val ministatement: List<Ministatement> = listOf()
)