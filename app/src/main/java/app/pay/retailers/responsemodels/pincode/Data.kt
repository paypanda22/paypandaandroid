package app.pay.retailers.responsemodels.pincode


import androidx.annotation.Keep

@Keep
data class Data(
    val city: String = "",
    val district: String = "",
    val pincode: Int = 0,
    val state: String = "",
    val sub_distance: String = ""
)