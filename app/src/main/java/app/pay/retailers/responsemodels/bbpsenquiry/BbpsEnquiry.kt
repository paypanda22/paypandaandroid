package app.pay.retailers.responsemodels.bbpsenquiry


import androidx.annotation.Keep

@Keep
data class BbpsEnquiry(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: String = ""
)