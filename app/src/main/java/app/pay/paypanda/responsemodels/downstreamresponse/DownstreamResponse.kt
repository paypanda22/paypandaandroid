package app.pay.paypanda.responsemodels.downstreamresponse

data class DownstreamResponse (
    val data: List<Data> = listOf(),
    val error: Boolean=true,
    val statusCode: Int=0,
    val totalCount: Int=0,

)