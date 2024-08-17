package app.pay.panda.responsemodels.PackageServices


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val name: String = "",
    val slots: List<Slot> = listOf()
)