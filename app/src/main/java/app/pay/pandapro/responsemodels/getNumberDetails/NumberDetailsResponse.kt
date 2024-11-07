package app.pay.pandapro.responsemodels.getNumberDetails

data class NumberDetailsResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int =0
)