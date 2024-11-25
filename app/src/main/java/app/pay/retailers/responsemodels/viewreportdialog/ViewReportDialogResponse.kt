package app.pay.retailers.responsemodels.viewreportdialog



data class ViewReportDialogResponse (
    val data:MutableList<Data>?,
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)