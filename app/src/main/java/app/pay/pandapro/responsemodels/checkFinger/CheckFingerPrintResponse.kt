package app.pay.pandapro.responsemodels.checkFinger

data class CheckFingerPrintResponse(
    val `data`: Data = Data(),
    val error: Boolean=true,
    val errorCode: Int=0,
    val message: String=""
)