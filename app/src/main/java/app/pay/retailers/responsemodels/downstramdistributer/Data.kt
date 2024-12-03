package app.pay.retailers.responsemodels.downstramdistributer


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val is_approved: Boolean = false,
    val main_wallet: String = "",
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val refer_id: String = "",
    val sponsor: List<Any> = listOf()
)