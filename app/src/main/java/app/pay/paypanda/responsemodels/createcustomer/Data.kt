package app.pay.paypanda.responsemodels.createcustomer

data class Data(
    val response: Response=Response(),
    val txnID: String=""
)