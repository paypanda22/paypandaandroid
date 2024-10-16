package app.pay.paypanda.responsemodels.downstreamRetailerResponse


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val is_approved: Boolean = false,
    val main_wallet: String = "",
    val name: String = "",
    val refer_id: String = "",
    val sponsor: Int = 0

)