package app.pay.panda.responsemodels.dashBoardData


import androidx.annotation.Keep

@Keep
data class DashBoardData(
    val `data`: Data? = Data(),
    val error: Boolean? = true,
    val statusCode: String? = ""
)