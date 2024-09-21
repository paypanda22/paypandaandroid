package app.pay.panda.responsemodels.aadharPayinvoice


import androidx.annotation.Keep

@Keep
data class AadharPayInvoiceResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)