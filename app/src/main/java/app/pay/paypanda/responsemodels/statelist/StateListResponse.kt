package app.pay.paypanda.responsemodels.statelist

data class StateListResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean=false,
    val statusCode: String=""
)