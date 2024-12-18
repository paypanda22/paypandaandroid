package app.pay.retailers.responsemodels.PackageServices


import androidx.annotation.Keep

@Keep
data class Slot(
    val _id: String = "",
    val charge: Int = 0,
    val charge_type: String = "",
    val commision: String = "",
    val commision_type: String = "",
    val distributor_comm: Int = 0,
    val distributor_comm_type: String = "",
    val end_amt: Int = 0,
    val isActive: Boolean = false,
    val master_distributer_com: Int = 0,
    val master_distributer_com_type: String = "",
    val start_amt: Int = 0
)