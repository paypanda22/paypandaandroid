package app.pay.panda.responsemodels.statelist

data class StateListResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean=false,
    val statusCode: String=""
)