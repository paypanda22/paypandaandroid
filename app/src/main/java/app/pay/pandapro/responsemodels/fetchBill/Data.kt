package app.pay.pandapro.responsemodels.fetchBill

data class Data(
    val billerResponse: BillerResponse = BillerResponse(),
    val refId: String=""
)