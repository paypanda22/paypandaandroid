package app.pay.panda.responsemodels.verifyBankName

data class Data(
    val accountStatus: String="",
    val accountStatusCode: String="",
    val `data`: DataX=DataX(),
    val message: String="",
    val status: String="",
    val subCode: String=""
)