package app.pay.panda.responsemodels.verifyBankName

data class DataX(
    val bankName: String="",
    val branch: String="",
    val city: String="",
    val micr: Int=0,
    val nameAtBank: String="",
    val refId: String="",
    val utr: String=""
)