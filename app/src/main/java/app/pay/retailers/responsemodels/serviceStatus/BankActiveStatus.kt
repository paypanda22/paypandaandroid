package app.pay.retailers.responsemodels.serviceStatus


import androidx.annotation.Keep

@Keep
data class BankActiveStatus(
    val Bank2: Boolean = false,
    val Bank3: Boolean = false
)