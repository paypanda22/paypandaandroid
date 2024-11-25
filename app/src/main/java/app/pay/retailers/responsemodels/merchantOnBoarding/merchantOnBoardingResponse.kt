package app.pay.retailers.responsemodels.merchantOnBoarding


import androidx.annotation.Keep

@Keep
data class merchantOnBoardingResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)