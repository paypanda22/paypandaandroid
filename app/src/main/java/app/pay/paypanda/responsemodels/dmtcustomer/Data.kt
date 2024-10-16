package app.pay.paypanda.responsemodels.dmtcustomer

data class Data(
    val response: Response = Response(),
    val txnID: String=""
)