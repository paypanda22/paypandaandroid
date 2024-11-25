package app.pay.retailers.responsemodels.general


import androidx.annotation.Keep

@Keep
data class GeneralResponse(
    val `data`: Data? = Data(),
    val error: Boolean? = true,
    val message: String? = "",
    val statusCode: String? = ""
)