package app.pay.paypanda.responsemodels.login

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    val `data`: Data? = Data(),
    val error: Boolean? = true,
    val message: String? = "",
    val statusCode: Int? = 0
)