package app.pay.retailers.responsemodels.packagedetail


import androidx.annotation.Keep

@Keep
data class Data(
    val BBPSWiseServices: List<BBPSWiseService> = listOf(),
    val OtherComm: List<OtherComm> = listOf(),
    val _id: String = "",
    val banner_img: Any = Any(),
    val description: String = "",
    val history: Any = Any(),
    val icon_img: String = "",
    val isPaid: Boolean = false,
    val isPurchased: Boolean = false,
    val package_name: String = "",
    val prices: List<Price> = listOf()
)