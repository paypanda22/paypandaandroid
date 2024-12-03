package app.pay.retailers.responsemodels.serviceStatus


import androidx.annotation.Keep

@Keep
data class Aeps2(
    val authRegistered: Boolean = false,
    val bankActiveStatus: BankActiveStatus = BankActiveStatus(),
    val dailyAuth: Boolean = false,
    val is_onboarding: Boolean = false
)