package app.pay.panda.responsemodels.checkFinger

data class CheckFingerPrintResponse(
    val `data`: Data = Data(),
    val error: Boolean=true,
    val errorCode: Int=0,
    val message: String=""
)