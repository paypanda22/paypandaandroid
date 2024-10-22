package app.pay.pandapro.responsemodels.adharpayresponse


import androidx.annotation.Keep

@Keep
data class Data(
    val __v: Int = 0,
    val _id: String = "",
    val aadhar_no: Long = 0,
    val amount: Int = 0,
    val bank_name: String = "",
    val createdAt: String = "",
    val isThreeWay: Boolean = false,
    val last_adhar: Int = 0,
    val nationalbankidentification: String = "",
    val req_status: Int = 0,
    val status: Int = 0,
    val txnStatus: Int = 0,
    val txn_id: String = "",
    val updatedAt: String = "",
    val user_id: String = "",
    val response: String = "",
    val status_update_time: String ?= null,
    val distributor_mobile: String ?= null
)