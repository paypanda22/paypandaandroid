package app.pay.paypanda.responsemodels.packageListResponse

data class Price(
    val _id: String?="",
    val duration: Int?=0,
    val duration_type: String?="",
    val mrp: Int?=0,
    val sale_rate: Int?=0,
    val tax: Int?=0,
    val tax_type: String?="",
    val real_value: Double?=0.0
)