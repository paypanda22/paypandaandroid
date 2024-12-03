package app.pay.retailers.responsemodels.fetchBill

data class Data(
    val billerResponse: BillerResponse = BillerResponse(),
    val refId: String=""
)