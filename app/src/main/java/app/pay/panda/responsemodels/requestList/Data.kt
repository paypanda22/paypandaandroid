package app.pay.panda.responsemodels.requestList

data class Data(
    val requestList: List<Request> = listOf(),
    val totalCount: Int=0
)