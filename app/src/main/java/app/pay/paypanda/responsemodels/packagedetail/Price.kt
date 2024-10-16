package app.pay.paypanda.responsemodels.packagedetail


import androidx.annotation.Keep

@Keep
data class Price(
    val _id: String = "",
    val isPaid: Boolean = false
)