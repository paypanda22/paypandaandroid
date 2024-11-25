package app.pay.retailers.responsemodels.packagedetail


import androidx.annotation.Keep

@Keep
data class Price(
    val _id: String = "",
    val isPaid: Boolean = false
)