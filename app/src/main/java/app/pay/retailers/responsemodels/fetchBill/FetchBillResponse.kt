package app.pay.retailers.responsemodels.fetchBill

data class FetchBillResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)