package app.pay.retailers.responsemodels.addRecipient

data class Data(
    val response: Response=Response(),
    val txnID: String="",
    val recipient_id: String=""
)