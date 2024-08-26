package app.pay.panda.responsemodels.cmsreportresponse


import androidx.annotation.Keep

@Keep
data class Data(
    val __v: Int = 0,
    val _id: String = "",
    val ackno: String = "",
    val amount: Int = 0,
    val biller_id: String = "",
    val biller_name: String = "",
    val createdAt: String = "",
    val current_status: String = "",
    val lat: String = "",
    val long: String = "",
    val mobile_no: String = "",
    val status: Int = 0,
    val txn_id: String = "",
    val txn_status: Int = 0,
    val unique_id: String = "",
    val updatedAt: String = "",
    val user_id: String = "",
    val wallet_debited: Boolean = false
)