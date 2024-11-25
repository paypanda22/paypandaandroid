package app.pay.retailers.responsemodels.aepsenquiry


import androidx.annotation.Keep

@Keep
data class AepsEnquiryResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)