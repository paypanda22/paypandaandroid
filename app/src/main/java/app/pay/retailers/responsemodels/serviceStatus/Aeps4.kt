package app.pay.retailers.responsemodels.serviceStatus


import androidx.annotation.Keep

@Keep
data class Aeps4(
    val authRegistered: Boolean = false,
    val bankActiveStatus: BankActiveStatusX = BankActiveStatusX(),
    val dailyAuth: Boolean = false,
    val is_onboarding: Boolean = false
)