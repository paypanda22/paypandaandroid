package app.pay.retailers.responsemodels.packagehistory


import androidx.annotation.Keep

@Keep
data class Price(
    val _id: String = "",
    val duration: Int = 0,
    val duration_type: String = "",
    val isPaid: Boolean = false,
    val mrp: Int = 0,
    val sale_rate: Int = 0,
    val tax: Int = 0,
    val tax_type: String = ""
)