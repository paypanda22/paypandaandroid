package app.pay.panda.responsemodels.fetchBill

data class Data(
    val billerResponse: BillerResponse = BillerResponse(),
    val refId: String=""
)