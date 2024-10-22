package app.pay.pandapro.responsemodels.realvalueresponse


import androidx.annotation.Keep

@Keep
data class Commision(
    val __v: Int = 0,
    val _id: String = "",
    val charge: Double = 0.0,
    val charge_type: String = "",
    val commision: Double = 0.0,
    val commision_type: String = "",
    val createdBy: String = "",
    val distributor_comm: Double = 0.0,
    val distributor_comm_type: String = "",
    val dmt_comm_id: String = "",
    val end_amt: Int = 0,
    val isActive: Boolean = false,
    val master_distributer_com: Double = 0.0,
    val master_distributer_com_type: String = "",
    val operator_id: List<Any> = listOf(),
    val start_amt: Int = 0,
    val updatedBy: String = ""
)