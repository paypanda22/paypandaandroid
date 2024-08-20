package app.pay.panda.responsemodels.downstreamRetailerResponse

data class DownstreamRetailarResp (
    val data: List<Data> = listOf(),
    val error: Boolean=true,
    val statusCode: Int=0,
    val totalCount: Int=0,

)