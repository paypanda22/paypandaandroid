package app.pay.retailers.responsemodels.singleutility



data class SingleUtilityTransaction (
    val `data`: Data = Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)