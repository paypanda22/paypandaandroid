package app.pay.panda.responsemodels.mAtmInit


import androidx.annotation.Keep

@Keep
data class Data(
    val api_user_name: String = "",
    val application_type: String = "",
    val brand_name: String = "",
    val clientRef_id: String = "",
    val client_id: String = "",
    val client_secret: String = "",
    val device_name: String = "",
    val device_type: String = "",
    val latitude: String = "",
    val login_id: String = "",
    val longitude: String = "",
    val paramB: String = "",
    val paramC: String = "",
    val shop_name: String = "",
    val skip_receipt: Boolean = false,
    val theme_color: String = "",
    val transaction_amount: String = "",
    val transaction_type: String = "",
    val user_mobile_number: String = "",
    val user_name: String = ""
)