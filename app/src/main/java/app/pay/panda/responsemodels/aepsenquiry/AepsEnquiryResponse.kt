package app.pay.panda.responsemodels.aepsenquiry


import androidx.annotation.Keep

@Keep
data class AepsEnquiryResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)