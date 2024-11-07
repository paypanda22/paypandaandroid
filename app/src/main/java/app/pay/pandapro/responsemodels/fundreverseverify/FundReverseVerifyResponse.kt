package app.pay.pandapro.responsemodels.fundreverseverify



data class FundReverseVerifyResponse (
    val `data`: Data = Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)