package app.pay.panda.responsemodels.getOperators

data class GetOperatorsResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean=true,
    val statusCode: String=""
)