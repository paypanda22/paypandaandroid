package app.pay.pandapro.responsemodels.pincode

data class PinCodeResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val errorCode: Int=0,
    val message: String=""
)