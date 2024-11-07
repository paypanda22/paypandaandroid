package app.pay.pandapro.responsemodels.fetchBill

data class BillerResponse(
    val amount: String="",
    val billDate: String="",
    val billNumber: String="",
    val billPeriod: String="",
    val billTags: List<Any> = listOf(),
    val custConvDesc: String="",
    val custConvFee: String="",
    val customerName: String="",
    val dueDate: String=""
)