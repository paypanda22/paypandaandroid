package app.pay.panda.responsemodels.createcustomer

data class CreateCustomerResponse(
    val `data`: Data=Data(),
    val error: Boolean=false,
    val message: String="",
    val statusCode: Int=0
)