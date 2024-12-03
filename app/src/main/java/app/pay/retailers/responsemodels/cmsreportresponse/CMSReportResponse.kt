package app.pay.retailers.responsemodels.cmsreportresponse


import androidx.annotation.Keep

@Keep
data class CMSReportResponse(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0,
    val totalCount: Int = 0
)