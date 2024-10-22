package app.pay.pandapro.responsemodels.packagehistory


import androidx.annotation.Keep

@Keep
data class PackageId(
    val __v: Int = 0,
    val _id: String = "",
    val banner_img: Any = Any(),
    val commisionSchema: List<CommisionSchema> = listOf(),
    val createdAt: String = "",
    val description: String = "",
    val icon_img: String = "",
    val isActive: Boolean = false,
    val isDelete: Boolean = false,
    val isPaid: Boolean = false,
    val package_name: String = "",
    val prices: List<Price> = listOf(),
    val services: String = "",
    val state: String = "",
    val updatedAt: String = ""
)