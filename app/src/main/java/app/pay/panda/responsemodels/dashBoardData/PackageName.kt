package app.pay.panda.responsemodels.dashBoardData


import androidx.annotation.Keep

@Keep
data class PackageName(
    val expiryDate: String? = "",
    val package_id: String? = "",
    val package_name: String? = ""
)