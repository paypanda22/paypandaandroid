package app.pay.retailers.responsemodels.dmtcharges


import androidx.annotation.Keep

@Keep
data class DMTChargesResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)