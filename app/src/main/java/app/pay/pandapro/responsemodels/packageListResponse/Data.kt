package app.pay.pandapro.responsemodels.packageListResponse

data class Data(
    val _id: String?="",
    val banner_img: Any? =null,
    val description: String?="",
    val history: Any? =null,
    val icon_img: String?="",
    val isPurchased: Boolean?=false,
    val isPaid: Boolean?=false,
    val package_name: String?="",
    val prices: List<Price>? = listOf(),
    val services: List<String> = listOf()
)