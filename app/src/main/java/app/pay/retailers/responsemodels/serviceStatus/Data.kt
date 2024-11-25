package app.pay.retailers.responsemodels.serviceStatus


import androidx.annotation.Keep

@Keep
data class Data(
    val Aeps2: Aeps2 = Aeps2(),
    val Aeps4: Aeps4 = Aeps4(),
    val is_active: Boolean = false,
    val is_buy: Boolean = false,
    val merchantCode: String = ""
)