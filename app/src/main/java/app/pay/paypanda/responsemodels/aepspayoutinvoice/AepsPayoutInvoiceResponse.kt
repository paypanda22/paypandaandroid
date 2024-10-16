package app.pay.paypanda.responsemodels.aepspayoutinvoice


import androidx.annotation.Keep

@Keep
data class AepsPayoutInvoiceResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)