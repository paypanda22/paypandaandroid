package app.pay.paypanda.responsemodels.aepsbank4


import androidx.annotation.Keep

@Keep
data class Bank4OnboardingResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)