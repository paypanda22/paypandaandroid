package app.pay.pandapro.responsemodels.requestList


import androidx.annotation.Keep

@Keep
data class Data(
    val requestList: List<Request> = listOf(),
    val totalCount: Int = 0
)