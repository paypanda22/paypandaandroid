package app.pay.paypanda.responsemodels.fundreverseresponse


import androidx.annotation.Keep

@Keep
data class FundReverseResendOtpResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)