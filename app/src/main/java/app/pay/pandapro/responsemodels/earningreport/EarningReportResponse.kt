package app.pay.pandapro.responsemodels.earningreport

data class EarningReportResponse (
    val `data`: Data = Data(),
    val error: Boolean=true,
    val statusCode: Int=0,

    )