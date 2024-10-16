package app.pay.paypanda.responsemodels.cmsinvoice


import androidx.annotation.Keep

@Keep
data class CMSInvoiceResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)