package app.pay.pandapro.responsemodels.serviceStatus

data class Data(
    val authRegistered: Boolean=false,
    val bank2: Boolean=false,
    val bank3: Boolean=false,
    val dailyAuth: Boolean=false,
    val isOnBoarded: Boolean=false,
    val is_active: Boolean=false,
    val is_buy: Boolean=false,
    val isInstantpayOnBoarded: Boolean? = null, // Nullable type
    val merchantCode: String="",

)