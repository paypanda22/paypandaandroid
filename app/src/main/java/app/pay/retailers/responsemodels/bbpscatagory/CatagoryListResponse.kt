package app.pay.retailers.responsemodels.bbpscatagory



data class CatagoryListResponse (
    val `data`: List<Data> = listOf(),
    val error: Boolean=true,
    val statusCode: Int=0
)