package app.pay.pandapro.responsemodels.packagehistory


import androidx.annotation.Keep

@Keep
data class PackageHistoryReportResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)