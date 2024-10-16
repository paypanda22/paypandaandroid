package app.pay.paypanda.responsemodels.paymentrequesttouser



data class PaymentrequesttouserResponse (
    val `data`: Data = Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)