package app.pay.paypanda.responsemodels.walletrequestlistsuper


import androidx.annotation.Keep

@Keep
data class RequestTo(
    val _id: String = "",
    val mobile: String = "",
    val name: String = "",
    val user_type_id: UserTypeId = UserTypeId()
)