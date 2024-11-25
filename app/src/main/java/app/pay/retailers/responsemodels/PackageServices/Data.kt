package app.pay.retailers.responsemodels.PackageServices


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val name: String = "",
    val slots: List<Slot> = listOf()
)