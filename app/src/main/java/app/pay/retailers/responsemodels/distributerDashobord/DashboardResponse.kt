package app.pay.retailers.responsemodels.distributerDashobord

data class DashboardResponse (
    val data: Data,
    val error: Boolean=true,
    val statusCode: Int=0
)