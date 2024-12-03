package app.pay.retailers.responsemodels.getOperators

data class Data(
    val _id: String="",
    val customerParams: List<CustomerParam> =listOf(),
    val fetchRequirement: String="",
    val name: String="",
    val operatorid: String,
    val paymentAmountExactness: String="",
    val supportBillValidation: String=""
)