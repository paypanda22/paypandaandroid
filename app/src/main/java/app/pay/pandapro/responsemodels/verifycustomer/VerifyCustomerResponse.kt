package app.pay.pandapro.responsemodels.verifycustomer

data class VerifyCustomerResponse(
    val `data`: Data=Data(),
    val error: Boolean=false,
    val message: String="",
    val statusCode: Int=0
)