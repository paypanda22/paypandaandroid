package app.pay.panda.responsemodels.billpayresponse

data class BillPayResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)