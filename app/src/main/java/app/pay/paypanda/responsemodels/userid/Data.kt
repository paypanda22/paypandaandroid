package app.pay.paypanda.responsemodels.userid


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val country: String = "",
    val email: String = "",
    val language_id: String = "",
    val locking_amt: String = "",
    val mobile: String = "",
    val name: String = "",
    val pinCode: String = "",
    val presentAddr: String = "",
    val profile: String = "",
    val refer_id: String = "",
    val state: String = "",
    val user_type_id: UserTypeId = UserTypeId()
)