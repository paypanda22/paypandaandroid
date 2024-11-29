package app.pay.retailers.responsemodels.dmtcharges


import androidx.annotation.Keep

@Keep
data class Data(
    val aeps_onBoardingCharge: Int = 0,
    val bankVerificationCharge: Int = 0,
    val minAepsToMainWallet: Int = 0
)