package app.pay.paypanda.responsemodels.payoutenquiry


import androidx.annotation.Keep

@Keep
data class PayoutEnquiryResponse(
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)