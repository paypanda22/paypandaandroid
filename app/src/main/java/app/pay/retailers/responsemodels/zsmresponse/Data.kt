package app.pay.retailers.responsemodels.zsmresponse


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val is_approved: Boolean = false,
    val main_wallet: String = "",
    val email: String = "",
    val mobile: String = "",
    val user_type: String = "",
    val name: String = "",
    val refer_id: String = "",
    val sponsor: List<Any> = listOf()
)