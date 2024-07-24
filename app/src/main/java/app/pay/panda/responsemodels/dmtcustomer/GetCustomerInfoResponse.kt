package app.pay.panda.responsemodels.dmtcustomer

data class GetCustomerInfoResponse(
    val `data`: Data = Data(),
    val error: Boolean=false,
    val message: String="",
    val statusCode: String="0"
)