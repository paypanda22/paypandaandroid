package app.pay.pandapro.responsemodels.packagehistory


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val amount: Double = 0.0,
    val createdAt: String = "",
    val duration: Int = 0,
    val duration_type: String = "",
    val mrp: Int = 0,
    val packageid: PackageId = PackageId(),
    val package_name: String = "",
    val package_priceId: Any = Any(),
    val refer_id: String = "",
    val services: List<String> = listOf(),
    val tax: Int = 0,
    val tax_type: String = "",
    val user_name: String = ""
)