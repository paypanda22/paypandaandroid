package app.pay.pandapro.responsemodels.aepsBanklist

data class AepsBankListResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean=true,
    val statusCode: Int=0
)