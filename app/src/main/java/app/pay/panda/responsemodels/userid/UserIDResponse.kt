package app.pay.panda.responsemodels.userid


import androidx.annotation.Keep

@Keep
data class UserIDResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)