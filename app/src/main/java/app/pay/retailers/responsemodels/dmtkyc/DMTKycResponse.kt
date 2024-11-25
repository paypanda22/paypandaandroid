package app.pay.retailers.responsemodels.dmtkyc


import androidx.annotation.Keep

@Keep
data class DMTKycResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)