package app.pay.paypanda.responsemodels.fetchBill

data class Data(
    val billerResponse: BillerResponse = BillerResponse(),
    val refId: String=""
)