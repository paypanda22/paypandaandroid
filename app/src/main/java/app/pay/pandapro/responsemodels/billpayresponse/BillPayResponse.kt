package app.pay.pandapro.responsemodels.billpayresponse

data class BillPayResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
    //val message: Int=0
)