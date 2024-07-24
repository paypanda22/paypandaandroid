package app.pay.panda.responsemodels.rechargePlans

data class RechargePlansResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)