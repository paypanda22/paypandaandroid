package app.pay.pandapro.responsemodels.aepscashwithdrawinvoice


import androidx.annotation.Keep

@Keep
data class CashWithdrawInvoiceResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val message: String = "",
    val statusCode: Int = 0
)